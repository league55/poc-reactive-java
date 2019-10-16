package services;

public interface ConnectionService {
    boolean enable(models.Connection configuration);
    boolean disable(models.Connection configuration);
}
