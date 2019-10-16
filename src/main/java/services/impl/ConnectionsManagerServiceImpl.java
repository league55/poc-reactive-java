package services.impl;

import api.ErrorResponse;
import static api.SuccessResponse.SUCCESS;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import models.Connection;
import persistance.ConnectionPersistence;
import services.ConnectionsManagerService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConnectionsManagerServiceImpl implements ConnectionsManagerService {

    private ConnectionPersistence persistence;
    private Vertx vertx;

    public ConnectionsManagerServiceImpl(ConnectionPersistence persistence, Vertx vertx) {
        this.persistence = persistence;
        this.vertx = vertx;
    }

    @Override
    public void getConnectionsList(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        List<JsonObject> results = persistence.getAll().stream()
                                              .map(Connection::toJson)
                                              .collect(Collectors.toList());
        OperationResponse response = OperationResponse.completedWithJson(new JsonArray(results));
        resultHandler.handle(Future.succeededFuture(response));
    }

    @Override
    public void createConnection(Connection body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        Connection transactionAdded = persistence.addConnection(body);
        resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(transactionAdded.toJson())));
    }

    @Override
    public void getConnection(String connectionId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        Optional<Connection> t = persistence.getConnection(connectionId);
        if (t.isPresent()) {
            resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(t.get().toJson())));
        } else {
            resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
        }
    }

    @Override
    public void updateConnection(String connectionId, Connection body, OperationRequest context,
            Handler<AsyncResult<OperationResponse>> resultHandler) {
        if (persistence.updateConnection(connectionId, body)) {
            resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(body.toJson())));
        } else {
            resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
        }
    }

    @Override
    public void deleteConnection(String connectionId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        if (persistence.removeConnection(connectionId)) {
            resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(JsonObject.mapFrom(SUCCESS))));
        } else {
            resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
        }
    }

    @Override
    public void enableConnection(String connectionId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        Optional<Connection> connection = persistence.getConnection(connectionId);
        if (connection.isPresent()) {
            ServerConnection sc = new ServerConnection(connection.get());
            vertx.deployVerticle(sc, event -> {
                if (event.succeeded()) {
                    resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(JsonObject.mapFrom(SUCCESS))));
                } else {
                    resultHandler.handle(Future.failedFuture(new ErrorResponse(500, "Internal Server Error")));
                }
            });
        } else {
            resultHandler.handle(Future.failedFuture(new ErrorResponse(404, "Not found")));
        }
    }

}
