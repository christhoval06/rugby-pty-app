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

import app.christhoval.rugbypty.utilities.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_id",
        "name",
        "updatedBy",
        "updatedAt",
        "createdBy",
        "createdAt",
        "autokey",
        "group",
        "matchType",
        "division",
        "tournament",
        "__v",
        "active",
        "teamB",
        "teamA",
        "matchDate",
        "wasPlayed"
})
public class Match implements Serializable {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("updatedBy")
    private String updatedBy;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("createdBy")
    private Object createdBy;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("autokey")
    private String autokey;
    @JsonProperty("group")
    private String group;
    @JsonProperty("matchType")
    private String matchType;
    @JsonProperty("division")
    private String division;
    //@JsonProperty("matchDay")
    @JsonIgnore
    private MatchDay matchDay;
    @JsonProperty("tournament")
    private String tournament;
    @JsonProperty("__v")
    private Integer v;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("teamB")
    private TeamMatch teamB;
    @JsonProperty("teamA")
    private TeamMatch teamA;
    @JsonProperty("matchDate")
    private String matchDate;
    @JsonProperty("wasPlayed")
    private Boolean wasPlayed;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonIgnore
    private boolean isExpanded = false;


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

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @JsonProperty("updatedAt")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("createdBy")
    public Object getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("autokey")
    public String getAutokey() {
        return autokey;
    }

    @JsonProperty("autokey")
    public void setAutokey(String autokey) {
        this.autokey = autokey;
    }

    @JsonProperty("group")
    public String getGroup() {
        return group;
    }

    @JsonProperty("group")
    public void setGroup(String group) {
        this.group = group;
    }

    @JsonProperty("matchType")
    public String getMatchType() {
        return matchType;
    }

    @JsonProperty("matchType")
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    @JsonProperty("division")
    public String getDivision() {
        return division;
    }

    @JsonProperty("division")
    public void setDivision(String division) {
        this.division = division;
    }

    @JsonProperty("tournament")
    public String getTournament() {
        return tournament;
    }

    @JsonProperty("tournament")
    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    @JsonProperty("__v")
    public Integer getV() {
        return v;
    }

    @JsonProperty("__v")
    public void setV(Integer v) {
        this.v = v;
    }

    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty("teamB")
    public TeamMatch getTeamB() {
        return teamB;
    }

    @JsonProperty("teamB")
    public void setTeamB(TeamMatch teamB) {
        this.teamB = teamB;
    }

    @JsonProperty("teamA")
    public TeamMatch getTeamA() {
        return teamA;
    }

    @JsonProperty("teamA")
    public void setTeamA(TeamMatch teamA) {
        this.teamA = teamA;
    }

    @JsonProperty("matchDate")
    public String getMatchDate() {
        return Utils.formatDate(matchDate);
    }

    @JsonProperty("matchDate")
    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    @JsonProperty("wasPlayed")
    public Boolean getWasPlayed() {
        return wasPlayed;
    }

    @JsonProperty("wasPlayed")
    public void setWasPlayed(Boolean wasPlayed) {
        this.wasPlayed = wasPlayed;
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
    public MatchDay getMatchDay() {
        return matchDay;
    }

    @JsonAnySetter
    public boolean isWasPlayed() {
        return wasPlayed;
    }

    @JsonAnySetter
    public void setMatchDay(MatchDay matchDay) {
        this.matchDay = matchDay;
    }

    @JsonAnyGetter
    public String getMatchDateWithFormat(String format) {
        return Utils.formatDate(matchDate, format);
    }

    @JsonAnyGetter
    public String getTeamAPoints() {
        if (teamA.getIsForfeit()) {
            return "F";
        } else {
            if (teamB.getIsForfeit())
                return "";
        }
        return String.valueOf(teamA.getPoints());
    }

    @JsonAnyGetter
    public String getTeamBPoints() {
        if (teamB.getIsForfeit()) {
            return "F";
        } else {
            if (teamA.getIsForfeit())
                return "";
        }
        return String.valueOf(teamB.getPoints());
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
