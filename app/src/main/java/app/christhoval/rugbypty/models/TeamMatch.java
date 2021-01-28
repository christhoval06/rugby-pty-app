package app.christhoval.rugbypty.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "teamRef",
        "team",
        "yellowCards",
        "redCards",
        "penals",
        "convertions",
        "tryes",
        "points",
        "isLoser",
        "isWinner",
        "isForfeit"
})
public class TeamMatch implements Serializable {

    @JsonProperty("teamRef")
    private Team teamRef;
    @JsonProperty("team")
    private String team;
    @JsonProperty("yellowCards")
    private Integer yellowCards;
    @JsonProperty("redCards")
    private Integer redCards;
    @JsonProperty("penals")
    private Integer penals;
    @JsonProperty("convertions")
    private Integer convertions;
    @JsonProperty("tryes")
    private Integer tryes;
    @JsonProperty("points")
    private Integer points;
    @JsonProperty("isLoser")
    private Boolean isLoser;
    @JsonProperty("isWinner")
    private Boolean isWinner;
    @JsonProperty("isForfeit")
    private Boolean isForfeit;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonIgnore
    private boolean isExpanded = false;


    @JsonProperty("teamRef")
    public Team getTeamRef() {
        return teamRef;
    }

    @JsonProperty("teamRef")
    public void setTeamRef(Team teamRef) {
        this.teamRef = teamRef;
    }

    @JsonProperty("team")
    public String getTeam() {
        return team;
    }

    @JsonProperty("team")
    public void setTeam(String team) {
        this.team = team;
    }

    @JsonProperty("yellowCards")
    public Integer getYellowCards() {
        return yellowCards;
    }

    @JsonProperty("yellowCards")
    public void setYellowCards(Integer yellowCards) {
        this.yellowCards = yellowCards;
    }

    @JsonProperty("redCards")
    public Integer getRedCards() {
        return redCards;
    }

    @JsonProperty("redCards")
    public void setRedCards(Integer redCards) {
        this.redCards = redCards;
    }

    @JsonProperty("penals")
    public Integer getPenals() {
        return penals;
    }

    @JsonProperty("penals")
    public void setPenals(Integer penals) {
        this.penals = penals;
    }

    @JsonProperty("convertions")
    public Integer getConvertions() {
        return convertions;
    }

    @JsonProperty("convertions")
    public void setConvertions(Integer convertions) {
        this.convertions = convertions;
    }

    @JsonProperty("tryes")
    public Integer getTryes() {
        return tryes;
    }

    @JsonProperty("tryes")
    public void setTryes(Integer tryes) {
        this.tryes = tryes;
    }

    @JsonProperty("points")
    public Integer getPoints() {
        return points;
    }

    @JsonProperty("points")
    public void setPoints(Integer points) {
        this.points = points;
    }

    @JsonProperty("isLoser")
    public Boolean getIsLoser() {
        return isLoser;
    }

    @JsonProperty("isLoser")
    public void setIsLoser(Boolean isLoser) {
        this.isLoser = isLoser;
    }

    @JsonProperty("isWinner")
    public Boolean getIsWinner() {
        return isWinner;
    }

    @JsonProperty("isWinner")
    public void setIsWinner(Boolean isWinner) {
        this.isWinner = isWinner;
    }

    @JsonProperty("isForfeit")
    public Boolean getIsForfeit() {
        return isForfeit;
    }

    @JsonProperty("isForfeit")
    public void setIsForfeit(Boolean isForfeit) {
        this.isForfeit = isForfeit;
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
    public boolean isExpanded() {
        return isExpanded;
    }
    @JsonAnySetter
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
