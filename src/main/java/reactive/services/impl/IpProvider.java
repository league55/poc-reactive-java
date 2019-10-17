package reactive.services.impl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import reactive.models.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactive.services.ProviderService;

public class IpProvider extends AbstractVerticle implements ProviderService {
    private static final Logger logger = LoggerFactory.getLogger(IpProvider.class);
    private NetSocket socket;
    private Provider configuration;

    public IpProvider(Provider configuration) {
        this.configuration = configuration;
    }

    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();
        NetClientOptions options = new NetClientOptions().setConnectTimeout(10000).setLocalAddress(configuration.getHost());
        NetClient client = vertx.createNetClient(options);
        client.connect(configuration.getPort(), configuration.getHost(), res -> {
            if (res.succeeded()) {
                logger.info("Provider connected successfully!");
                socket = res.result();
                configuration.setEnabled(true);
                registerEventListener(configuration.getId());
                future.succeeded();
            } else {
                logger.warn("Failed to connect: " + res.cause().getMessage());
                configuration.setEnabled(false);
                future.failed();
            }
        });
    }

    private void registerEventListener(String id) {
        EventBus eventBus = vertx.eventBus();

        logger.info("Registering new event handler " + id);
        eventBus.consumer(id, message -> {
            logger.info(String.valueOf(message.body()));
            this.getInfo(String.valueOf(message.body()))
                .setHandler(s -> {
                    message.reply(s.result());
                    s.succeeded();
                });
        });
    }

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
