package app.christhoval.rugbypty.models.response;

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

import app.christhoval.rugbypty.models.Match;
import app.christhoval.rugbypty.models.MatchDay;
import app.christhoval.rugbypty.models.Tournament;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tournament",
        "matchDays",
        "matchs"
})
public class FixturesResults {

    @JsonProperty("tournament")
    private Tournament tournament;
    @JsonProperty("matchDays")
    private List<MatchDay> matchDays = null;
    @JsonProperty("matchs")
    private List<Object> matchs = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tournament")
    public Tournament getTournament() {
        return tournament;
    }

    @JsonProperty("tournament")
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    @JsonProperty("matchDays")
    public List<MatchDay> getMatchDays() {
        return matchDays;
    }

    @JsonProperty("matchDays")
    public void setMatchDays(List<MatchDay> matchDays) {
        this.matchDays = matchDays;
    }

    @JsonProperty("matchs")
    public List<Object> getMatchs() {
        return matchs;
    }

    @JsonProperty("matchs")
    public void setMatchs(List<Object> matchs) {
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
}
