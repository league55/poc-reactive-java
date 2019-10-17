package reactive.services;

import io.vertx.core.Future;
import io.vertx.core.Verticle;

public interface ProviderService extends Verticle {
    Future<String> getInfo(String input);
}
