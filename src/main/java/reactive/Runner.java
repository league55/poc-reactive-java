package reactive;

import reactive.api.WebApiService;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.log4j.BasicConfigurator;

public class Runner {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        InternalLoggerFactory.setDefaultFactory(Log4JLoggerFactory.INSTANCE);
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

        vertx.deployVerticle(new WebApiService());

    }
}
