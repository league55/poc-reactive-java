package reactive.services.broker.impl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import reactive.models.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactive.services.broker.ConnectionService;

public class ServerConnection extends AbstractVerticle implements ConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(ServerConnection.class);

    private Connection configuration;
    public ServerConnection(Connection configuration) {
        this.configuration = configuration;
    }

    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();
        NetServerOptions options = new NetServerOptions().setLogActivity(true);
        NetServer server = vertx.createNetServer(options);

        server.connectHandler(socket -> onInput(configuration, socket))
              .listen(configuration.getPort(), configuration.getHost(), connectionStartedHandler(future, configuration));
    }

    private Handler<AsyncResult<NetServer>> connectionStartedHandler(Future<Void> future, Connection configuration) {
        return res -> {
            if (res.succeeded()) {
                logger.info(String
                        .format("Server %s is now listening %s:%d%n", configuration.getId(), configuration.getHost(),
                                configuration.getPort()));
                configuration.setEnabled(true);
                future.succeeded();
            } else {
                logger.warn("Failed to bind!", res.cause());
                future.failed();
            }
        };
    }

    private void onInput(Connection configuration, NetSocket socket) {
        logger.info("New connection to " + configuration.getId());
        socket.handler(buffer -> {
            String message = buffer.getString(0, buffer.length());
            logger.info("Got input: " + message);
            EventBus eventBus = vertx.eventBus();

            logger.info("Sending message to " + configuration.getProviderAddress() + " : " + message);
            eventBus.request(configuration.getProviderAddress(), message, ar -> {
                String result = ar.succeeded() ? ar.result().body().toString() + "\r\n" : "Error";
                logger.info("Received reply: " + result);
                socket.write(result);
            });
        });
    }


}
