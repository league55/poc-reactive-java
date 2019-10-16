package models;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Provider {
    protected String id;
    protected boolean enabled;
    protected String host;
    protected int port;

}
