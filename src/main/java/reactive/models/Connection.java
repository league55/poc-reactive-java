package reactive.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Connection {
    protected String id;
    protected String host;
    protected int port;
    protected boolean enabled;
    protected String providerAddress;
}
