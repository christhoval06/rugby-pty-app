package app.christhoval.rugbypty.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "brief",
        "extended"
})
public class Content implements Serializable {
    @JsonProperty("brief")
    private String brief;
    @JsonProperty("extended")
    private String extended;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("brief")
    public String getBrief() {
        return brief;
    }

    @JsonProperty("brief")
    public void setBrief(String brief) {
        this.brief = brief;
    }

    @JsonProperty("extended")
    public String getExtended() {
        return extended;
    }

    @JsonProperty("extended")
    public void setExtended(String extended) {
        this.extended = extended;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}