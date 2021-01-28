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

import app.christhoval.rugbypty.models.TeamInTournament;
import app.christhoval.rugbypty.models.Tournament;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tournament",
        "teams"
})
public class PositionsTable {
    @JsonProperty("tournament")
    private Tournament tournament;
    @JsonProperty("teams")
    private List<TeamInTournament> teams = null;
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

    @JsonProperty("teams")
    public List<TeamInTournament> getTeams() {
        return teams;
    }

    @JsonProperty("teams")
    public void setTeams(List<TeamInTournament> teams) {
        this.teams = teams;
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
