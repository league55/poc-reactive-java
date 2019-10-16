package models;

import lombok.Builder;
import lombok.Data;
import services.ProviderService;

@Data
@Builder
public class Connection {
    private String id;
    private String host;
    private int port;
    private boolean enabled;
    private ProviderService providerService;
}
