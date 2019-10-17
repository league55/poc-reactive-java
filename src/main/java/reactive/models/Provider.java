package reactive.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;


@DataObject(generateConverter = true, publicConverter = false)
public class Provider {

    public Provider(String id, boolean enabled, String host, int port) {
        this.id = id;
        this.enabled = enabled;
        this.host = host;
        this.port = port;
    }

    public Provider() {
    }

    public Provider(JsonObject json) {
        ProviderConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ProviderConverter.toJson(this, json);
        return json;
    }

    protected String id;
    protected boolean enabled;
    protected String host;
    protected int port;

    public String getId() {
        return id;
    }

    @Fluent public Provider setId(String id) {
        this.id = id;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Fluent public Provider setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getHost() {
        return host;
    }

    @Fluent public Provider setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    @Fluent public Provider setPort(int port) {
        this.port = port;
        return this;
    }
}
