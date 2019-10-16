package services.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import models.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.ProviderService;

public class IpProvider implements ProviderService {
    private static final Logger logger = LoggerFactory.getLogger(IpProvider.class);
    private Vertx vertx;
    private NetSocket socket;

    public IpProvider(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public boolean enable(Provider configuration) {
        NetClientOptions options = new NetClientOptions().setConnectTimeout(10000)
                                                         .setLocalAddress(configuration.getHost());
        NetClient client = vertx.createNetClient(options);
        client.connect(configuration.getPort(), configuration.getHost(), res -> {
            if (res.succeeded()) {
                logger.info("Provider connected successfully!");
                socket = res.result();
                configuration.setEnabled(true);
            } else {
                logger.warn("Failed to connect: " + res.cause().getMessage());
                configuration.setEnabled(false);
            }
        });

        return configuration.isEnabled();
    }

    @Override
    public boolean disable(Provider configuration) {
        return false;
    }

    @Override
    public Future<String> getInfo(String input) {
        logger.info("Providing for: " + input);
        socket.write(input);
        return Future.future(promise -> {
            socket.handler(buffer -> {
                promise.complete(buffer.getString(0, buffer.length()));
            });
        });
    }

}
