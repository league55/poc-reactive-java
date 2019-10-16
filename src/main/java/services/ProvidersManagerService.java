package services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.ext.web.api.generator.WebApiServiceGen;
import models.Provider;
import persistance.ProviderPersistence;
import services.impl.ProvidersManagerServiceImpl;

@WebApiServiceGen
public interface ProvidersManagerService {
    static ProvidersManagerService create(ProviderPersistence persistence, Vertx vertx) {
        return new ProvidersManagerServiceImpl(persistence, vertx);
    }

    void getProvidersList(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void createProvider(Provider body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void getProvider(String providerId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void updateProvider(String providerId, Provider body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void deleteProvider(String providerId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void enableProvider(String providerId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

}
