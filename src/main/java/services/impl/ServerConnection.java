package services.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServerOptions;
import models.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.ConnectionService;

public class ServerConnection implements ConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(ServerConnection.class);
    private Vertx vertx;

    public ServerConnection(Vertx vertx) {
        this.vertx = vertx;
    }


    @Override
    public boolean enable(Connection configuration) {
        NetServerOptions options = new NetServerOptions().setLogActivity(true);
        vertx.createNetServer(options)
             .connectHandler(socket -> {
                 socket.handler(buffer -> {
                     logger.info("Responding with provider info: " + buffer);
                     configuration.getProviderService()
                                  .getInfo(buffer.getString(0, buffer.length()))
                                  .compose(s -> {
                                      socket.write(s);
                                      socket.write("\r\n");
                                      return Future.succeededFuture();
                                  });
                 });
             })
             .listen(configuration.getPort(), configuration.getHost(), res -> {
                 if (res.succeeded()) {
                     logger.info(String.format("Server is now listening %s:%d", configuration.getHost(), configuration.getPort()));
                     configuration.setEnabled(true);
                 } else {
                     logger.warn("Failed to bind!");
                 }
             });


        return configuration.isEnabled();
    }

    @Override
    public boolean disable(Connection configuration) {
        return false;
    }

}
