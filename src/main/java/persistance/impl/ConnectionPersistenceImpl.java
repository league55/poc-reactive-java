package persistance.impl;

import models.Connection;
import persistance.ConnectionPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConnectionPersistenceImpl implements ConnectionPersistence {

    private Map<String, Connection> transactions;

    public ConnectionPersistenceImpl() {
        transactions = new HashMap<>();
    }

    @Override
    public List<Connection> getFilteredConnections(Predicate<Connection> p) {
        return transactions.values().stream().filter(p).collect(Collectors.toList());
    }

    @Override
    public List<Connection> getAll() {
        return new ArrayList<>(transactions.values());
    }

    @Override
    public Optional<Connection> getConnection(String connectionId) {
        return Optional.ofNullable(transactions.get(connectionId));
    }

    @Override
    public Connection addConnection(Connection t) {
        transactions.put(t.getId(), t);
        return t;
    }

    @Override
    public boolean removeConnection(String connectionId) {
        Connection t = transactions.remove(connectionId);
        return t != null;
    }

    @Override
    public boolean updateConnection(String connectionId, Connection transaction) {
        Connection t = transactions.replace(connectionId, transaction);
        return t != null;
    }
}
