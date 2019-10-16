package services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.ext.web.api.generator.WebApiServiceGen;
import models.Connection;
import persistance.ConnectionPersistence;
import services.impl.ConnectionsManagerServiceImpl;

@WebApiServiceGen
public interface ConnectionsManagerService {
    static ConnectionsManagerService create(ConnectionPersistence persistence, Vertx vertx) {
        return new ConnectionsManagerServiceImpl(persistence, vertx);
    }

    void getConnectionsList(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void createConnection(Connection body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void getConnection(String connectionId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void updateConnection(String connectionId, Connection body, OperationRequest context,
            Handler<AsyncResult<OperationResponse>> resultHandler);

    void deleteConnection(String connectionId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void enableConnection(String connectionId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

}
