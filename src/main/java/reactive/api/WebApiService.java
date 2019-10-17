package reactive.api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.serviceproxy.ServiceBinder;
import reactive.persistance.ConnectionPersistence;
import reactive.persistance.ProviderPersistence;
import reactive.services.api.ConnectionsManagerService;
import reactive.services.api.ProvidersManagerService;

public class WebApiService extends AbstractVerticle {

    HttpServer server;
    ServiceBinder serviceBinder;

    MessageConsumer<JsonObject> connectionsMessageConsumer;
    MessageConsumer<JsonObject> providersMessageConsumer;

    /**
     * Start connection service
     */
    private void startConnectionService() {
        serviceBinder = new ServiceBinder(vertx);

        ConnectionPersistence connectionPersistence = ConnectionPersistence.create();
        ProviderPersistence providerPersistence = ProviderPersistence.create();

        // Create an instance of ConnectionManagerService and mount to event bus
        ConnectionsManagerService connectionsManagerService = ConnectionsManagerService.create(connectionPersistence, vertx);
        ProvidersManagerService providersManagerService = ProvidersManagerService.create(providerPersistence, vertx);

        connectionsMessageConsumer = serviceBinder
                .setAddress("connections_manager.myapp")
                .register(ConnectionsManagerService.class, connectionsManagerService);

        providersMessageConsumer = serviceBinder
                .setAddress("providers_manager.myapp")
                .register(ProvidersManagerService.class, providersManagerService);
    }

    /**
     * This method constructs the router factory, mounts reactive.services and handlers and starts the http server with built router
     * @return
     */
    private Future<Void> startHttpServer() {
        Future<Void> future = Future.future();
        OpenAPI3RouterFactory.create(this.vertx, "/openapi.json", openAPI3RouterFactoryAsyncResult -> {
            if (openAPI3RouterFactoryAsyncResult.succeeded()) {
                OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();

                // Mount reactive.services on event bus based on extensions
                routerFactory.mountServicesFromExtensions();

                // Generate the router
                Router router = routerFactory.getRouter();
                server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
                server.requestHandler(router).listen(ar -> {
                    // Error starting the HttpServer
                    if (ar.succeeded()) future.complete();
                    else future.fail(ar.cause());
                });
            } else {
                // Something went wrong during router factory initialization
                future.fail(openAPI3RouterFactoryAsyncResult.cause());
            }
        });
        return future;
    }

    @Override
    public void start(Future<Void> future) {
        startConnectionService();
        startHttpServer().setHandler(future.completer());
    }

    /**
     * This method closes the http server and unregister all reactive.services loaded to Event Bus
     */
    @Override
    public void stop(){
        this.server.close();
        connectionsMessageConsumer.unregister();
    }
}
