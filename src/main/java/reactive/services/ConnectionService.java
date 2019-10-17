package reactive.services;

public interface ConnectionService {
    boolean enable(reactive.models.Connection configuration);
    boolean disable(reactive.models.Connection configuration);
}
