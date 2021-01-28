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
        "_id",
        "name",
        "division",
        "team",
        "gp",
        "gw",
        "gd",
        "gl",
        "lf",
        "tf",
        "tc",
        "td",
        "cr",
        "pr",
        "tp"
})
public class TeamInTournament implements Serializable {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("division")
    private String division;
    @JsonProperty("team")
    private Team team;
    @JsonProperty("gp")
    private Integer gp;
    @JsonProperty("gw")
    private Integer gw;
    @JsonProperty("gd")
    private Integer gd;
    @JsonProperty("gl")
    private Integer gl;
    @JsonProperty("lf")
    private Integer lf;
    @JsonProperty("tf")
    private Integer tf;
    @JsonProperty("tc")
    private Integer tc;
    @JsonProperty("td")
    private Integer td;
    @JsonProperty("cr")
    private Integer cr;
    @JsonProperty("pr")
    private Integer pr;
    @JsonProperty("tp")
    private Integer tp;
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

    @JsonProperty("division")
    public String getDivision() {
        return division;
    }

    @JsonProperty("division")
    public void setDivision(String division) {
        this.division = division;
    }

    @JsonProperty("team")
    public Team getTeam() {
        return team;
    }

    @JsonProperty("team")
    public void setTeam(Team team) {
        this.team = team;
    }

    @JsonProperty("gp")
    public Integer getGp() {
        return gp;
    }

    @JsonProperty("gp")
    public void setGp(Integer gp) {
        this.gp = gp;
    }

    @JsonProperty("gw")
    public Integer getGw() {
        return gw;
    }

    @JsonProperty("gw")
    public void setGw(Integer gw) {
        this.gw = gw;
    }

    @JsonProperty("gd")
    public Integer getGd() {
        return gd;
    }

    @JsonProperty("gd")
    public void setGd(Integer gd) {
        this.gd = gd;
    }

    @JsonProperty("gl")
    public Integer getGl() {
        return gl;
    }

    @JsonProperty("gl")
    public void setGl(Integer gl) {
        this.gl = gl;
    }

    @JsonProperty("lf")
    public Integer getLf() {
        return lf;
    }

    @JsonProperty("lf")
    public void setLf(Integer lf) {
        this.lf = lf;
    }

    @JsonProperty("tf")
    public Integer getTf() {
        return tf;
    }

    @JsonProperty("tf")
    public void setTf(Integer tf) {
        this.tf = tf;
    }

    @JsonProperty("tc")
    public Integer getTc() {
        return tc;
    }

    @JsonProperty("tc")
    public void setTc(Integer tc) {
        this.tc = tc;
    }

    @JsonProperty("td")
    public Integer getTd() {
        return td;
    }

    @JsonProperty("td")
    public void setTd(Integer td) {
        this.td = td;
    }

    @JsonProperty("cr")
    public Integer getCr() {
        return cr;
    }

    @JsonProperty("cr")
    public void setCr(Integer cr) {
        this.cr = cr;
    }

    @JsonProperty("pr")
    public Integer getPr() {
        return pr;
    }

    @JsonProperty("pr")
    public void setPr(Integer pr) {
        this.pr = pr;
    }

    @JsonProperty("tp")
    public Integer getTp() {
        return tp;
    }

    @JsonProperty("tp")
    public void setTp(Integer tp) {
        this.tp = tp;
    }

    @JsonAnyGetter
    public Integer getTryDiference() {
        return this.tf - this.tc;
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
    public String getGame() {
        return String.format("%1$s-%2$s-%3$s", gw, gl, gd);
    }

    @JsonAnyGetter
    public String getTries() {
        return String.format("%1$s / %2$s", tf, tc);
    }

    @JsonAnyGetter
    public String getPenalsAndConversions() {
        return String.format("%1$s / %2$s", pr, cr);
    }

    @JsonAnyGetter
    public boolean isExpanded() {
        return isExpanded;
    }

    @JsonAnyGetter
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
