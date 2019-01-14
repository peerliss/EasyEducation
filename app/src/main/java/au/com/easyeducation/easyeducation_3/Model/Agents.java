package au.com.easyeducation.easyeducation_3.Model;

import android.media.Image;

public class Agents {
    private String agent_name;
    private String agent_username;
    private String agent_description;
    private String agent_visa;
    private String agent_hours;
    private int agent_distance;
    private int agent_reviews;
    private int agent_rating;
    private Image agent_profile;

    public Agents(String agent_name, String agent_username, String agent_description, String agent_visa, String agent_hours, int agent_distance, int agent_reviews, int agent_rating/*, Image agent_profile*/) {
        this.agent_name = agent_name;
        this.agent_username = agent_username;
        this.agent_description = agent_description;
        this.agent_visa = agent_visa;
        this.agent_hours = agent_hours;
        this.agent_distance = agent_distance;
        this.agent_reviews = agent_reviews;
        this.agent_rating = agent_rating;
//        this.agent_profile = agent_profile;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public String getAgent_username() {
        return agent_username;
    }

    public String getAgent_description() {
        return agent_description;
    }

    public String getAgent_visa() {
        return agent_visa;
    }

    public String getAgent_hours() {
        return agent_hours;
    }

    public int getAgent_distance() {
        return agent_distance;
    }

    public int getAgent_reviews() {
        return agent_reviews;
    }

    public int getAgent_rating() {
        return agent_rating;
    }

    public Image getAgent_profile() {
        return agent_profile;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public void setAgent_username(String agent_username) {
        this.agent_username = agent_username;
    }

    public void setAgent_description(String agent_description) {
        this.agent_description = agent_description;
    }

    public void setAgent_visa(String agent_visa) {
        this.agent_visa = agent_visa;
    }

    public void setAgent_hours(String agent_hours) {
        this.agent_hours = agent_hours;
    }

    public void setAgent_distance(int agent_distance) {
        this.agent_distance = agent_distance;
    }

    public void setAgent_reviews(int agent_reviews) {
        this.agent_reviews = agent_reviews;
    }

    public void setAgent_rating(int agent_rating) {
        this.agent_rating = agent_rating;
    }

    public void setAgent_profile(Image agent_profile) {
        this.agent_profile = agent_profile;
    }
}
