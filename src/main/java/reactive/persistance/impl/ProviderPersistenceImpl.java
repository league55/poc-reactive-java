package reactive.persistance.impl;

import reactive.models.Provider;
import reactive.persistance.ProviderPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProviderPersistenceImpl implements ProviderPersistence {

    private Map<String, Provider> transactions;

    public ProviderPersistenceImpl() {
        transactions = new HashMap<>();
    }

    @Override
    public List<Provider> getFilteredProviders(Predicate<Provider> p) {
        return transactions.values().stream().filter(p).collect(Collectors.toList());
    }

    @Override
    public List<Provider> getAll() {
        return new ArrayList<>(transactions.values());
    }

    @Override
    public Optional<Provider> getProvider(String providerId) {
        return Optional.ofNullable(transactions.get(providerId));
    }

    @Override
    public Provider addProvider(Provider t) {
        transactions.put(t.getId(), t);
        return t;
    }

    @Override
    public boolean removeProvider(String providerId) {
        Provider t = transactions.remove(providerId);
        return t != null;
    }

    @Override
    public boolean updateProvider(String providerId, Provider transaction) {
        Provider t = transactions.replace(providerId, transaction);
        return t != null;
    }
}
