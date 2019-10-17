package reactive;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import reactive.models.Connection;
import reactive.models.Provider;
import org.apache.log4j.BasicConfigurator;
import reactive.services.ConnectionService;
import reactive.services.ProviderService;
import reactive.services.impl.IpProvider;
import reactive.services.impl.ServerConnection;

public class Runner {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        InternalLoggerFactory.setDefaultFactory(Log4JLoggerFactory.INSTANCE);
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

        Provider dataProviderConf = Provider.builder()
                                            .id("dataprov")
                                            .host("0.0.0.0")
                                            .port(9500)
                                            .enabled(false)
                                            .build();
        ProviderService providerService = new IpProvider(vertx);
        providerService.enable(dataProviderConf);

        Connection serverConnConfiguration = Connection.builder()
                                                       .id("server_conn")
                                                       .host("127.0.0.1")
                                                       .port(8100)
                                                       .enabled(false)
                                                       .providerService(providerService)
                                                       .build();

        ConnectionService serverConnection = new ServerConnection(vertx);
        serverConnection.enable(serverConnConfiguration);


    }
}
