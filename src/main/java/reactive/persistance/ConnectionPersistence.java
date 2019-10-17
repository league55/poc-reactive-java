package reactive.persistance;

import reactive.models.Connection;
import reactive.persistance.impl.ConnectionPersistenceImpl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ConnectionPersistence {
    /**
     * Factory method to instantiate ConnectionPersistence
     *
     * @return
     */
    static ConnectionPersistence create() {
        return new ConnectionPersistenceImpl();
    }

    List<Connection> getAll();

    List<Connection> getFilteredConnections(Predicate<Connection> p);

    Optional<Connection> getConnection(String connectionId);

    Connection addConnection(Connection t);

    boolean removeConnection(String connectionId);

    boolean updateConnection(String connectionId, Connection transaction);
}
