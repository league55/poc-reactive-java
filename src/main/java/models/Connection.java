package models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true, publicConverter = false)
public class Connection {
    protected String id;
    protected String host;
    protected int port;
    protected boolean enabled;
    protected String providerAddress;

    public Connection(String id, String host, int port, boolean enabled, String providerAddress) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.enabled = enabled;
        this.providerAddress = providerAddress;
    }

    public Connection() {
    }

    public Connection(JsonObject json) {
        ConnectionConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ConnectionConverter.toJson(this, json);
        return json;
    }

    public String getId() {
        return id;
    }

    @Fluent public Connection setId(String id) {
        this.id = id;
        return this;
    }

    public String getHost() {
        return host;
    }

    @Fluent public Connection setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    @Fluent public Connection setPort(int port) {
        this.port = port;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Fluent public Connection setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    @Fluent public Connection setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
        return this;
    }
}
