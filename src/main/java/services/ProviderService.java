package services;

import io.vertx.core.Future;
import models.Provider;

public interface ProviderService {
    boolean enable(Provider configuration);
    boolean disable(Provider configuration);

    Future<String> getInfo(String input);
}
