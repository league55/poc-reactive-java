package persistance;

import models.Provider;
import persistance.impl.ProviderPersistenceImpl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ProviderPersistence {
    /**
     * Factory method to instantiate ProviderPersistence
     *
     * @return
     */
    static ProviderPersistence create() {
        return new ProviderPersistenceImpl();
    }

    List<Provider> getAll();

    List<Provider> getFilteredProviders(Predicate<Provider> p);

    Optional<Provider> getProvider(String providerId);

    Provider addProvider(Provider t);

    boolean removeProvider(String providerId);

    boolean updateProvider(String providerId, Provider transaction);
}
