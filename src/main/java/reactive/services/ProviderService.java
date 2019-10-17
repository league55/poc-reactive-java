package reactive.services;

import io.vertx.core.Future;
import reactive.models.Provider;

public interface ProviderService {
    boolean enable(Provider configuration);
    boolean disable(Provider configuration);

    Future<String> getInfo(String input);
}
