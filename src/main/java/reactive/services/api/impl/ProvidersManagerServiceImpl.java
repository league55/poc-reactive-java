package reactive.services.api.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import reactive.models.Provider;
import reactive.persistance.ProviderPersistence;
import reactive.services.api.ProvidersManagerService;
import reactive.services.broker.impl.IpProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProvidersManagerServiceImpl implements ProvidersManagerService {

    private ProviderPersistence persistence;
    private Vertx vertx;

    public ProvidersManagerServiceImpl(ProviderPersistence persistence, Vertx vertx) {
        this.persistence = persistence;
        this.vertx = vertx;
    }

    @Override
    public void getProvidersList(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
        List<Provider> results = persistence.getAll();
        resultHandler.handle(Future.succeededFuture(
                OperationResponse.completedWithJson(
                        new JsonArray(results.stream().map(Provider::toJson).collect(Collectors.toList()))
                                                   )
                                                   ));
    }

    @Override
    public void createProvider(
            Provider body,
            OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
        Provider transactionAdded = persistence.addProvider(body);
        resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(transactionAdded.toJson())));
    }

    @Override
    public void getProvider(
            String providerId,
            OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
        Optional<Provider> t = persistence.getProvider(providerId);
        if (t.isPresent())
            resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(t.get().toJson())));
        else
            resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
    }

    @Override
    public void updateProvider(
            String providerId,
            Provider body,
            OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
        if (persistence.updateProvider(providerId, body))
            resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(body.toJson())));
        else
            resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
    }

    @Override
    public void deleteProvider(
            String providerId,
            OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
        if (persistence.removeProvider(providerId))
            resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(200).setStatusMessage("OK")));
        else
            resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
    }

    @Override
    public void enableProvider(
            String providerId,
            OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
        Optional<Provider> provider = persistence.getProvider(providerId);
        if (provider.isPresent()) {
            IpProvider sc = new IpProvider(provider.get());
            vertx.deployVerticle(sc, event -> {
                if(event.succeeded()) {
                    resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(200).setStatusMessage("Success")));
                } else {
                    resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(500).setStatusMessage("Failed to "
                                                                                                                            + "deploy")));
                }
            });
        } else
            resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
    }

}
