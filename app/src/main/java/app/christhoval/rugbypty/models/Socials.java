package app.christhoval.rugbypty.models;

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
        "facebook",
        "instagram",
        "twitter",
        "youtube"
})
public class Socials {

    @JsonProperty("facebook")
    private String facebook;
    @JsonProperty("instagram")
    private String instagram;
    @JsonProperty("twitter")
    private String twitter;
    @JsonProperty("youtube")
    private String youtube;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("facebook")
    public String getFacebook() {
        return facebook;
    }

    @JsonProperty("facebook")
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    @JsonProperty("instagram")
    public String getInstagram() {
        return instagram;
    }

    @JsonProperty("instagram")
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    @JsonProperty("twitter")
    public String getTwitter() {
        return twitter;
    }

    @JsonProperty("twitter")
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    @JsonProperty("youtube")
    public String getYoutube() {
        return youtube;
    }

    @JsonProperty("youtube")
    public void setYoutube(String youtube) {
        this.youtube = youtube;
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