package app.christhoval.rugbypty.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import app.christhoval.rugbypty.utilities.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_id",
        "name",
        "matchDate",
        "place",
        "matchs"
})
public class MatchDay implements Serializable {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("matchDate")
    private String matchDate;
    @JsonProperty("place")
    private String place;
    @JsonProperty("matchs")
    private List<Match> matchs = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonIgnore
    public boolean isExpanded = false;

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("matchDate")
    public String getMatchDate() {
        return Utils.formatDate(matchDate);
    }

    @JsonProperty("matchDate")
    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    @JsonProperty("place")
    public String getPlace() {
        return place;
    }

    @JsonProperty("place")
    public void setPlace(String place) {
        this.place = place;
    }

    @JsonProperty("matchs")
    public List<Match> getMatchs() {
        return matchs;
    }

    @JsonProperty("matchs")
    public void setMatchs(List<Match> matchs) {
        this.matchs = matchs;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonAnyGetter
    public String getMatchDateWithFormat(String format) {
        return Utils.formatDate(matchDate, format);
    }

    @JsonAnyGetter
    public long getMilliSeconds() {
        return Utils.formatDate2MilliSeconds(matchDate);
    }

    @JsonAnyGetter
    public boolean isExpanded() {
        return isExpanded;
    }

    @JsonAnySetter
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @JsonAnyGetter
    public boolean isDateGreatThanNow() {
        return Utils.isDateGreatThanNow(matchDate);
    }
}
