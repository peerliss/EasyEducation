package au.com.easyeducation.easyeducation_3.Model;

//import android.media.Image;

public class Institution {
    private String name;
    private String username;
    private String description;
    private String visa;
    private String hours;
    private String cricos;
    private double distance;
    private int reviews;
    private double rating;
//    private Image agent_profile;

    public Institution(){}

    public Institution(String name, String username, String description, String visa, String hours, String cricos, double distance, int reviews, double rating/*, Image agent_profile*/) {
        this.name = name;
        this.username = username;
        this.description = description;
        this.visa = visa;
        this.hours = hours;
        this.cricos = cricos;
        this.distance = distance;
        this.reviews = reviews;
        this.rating = rating;
//        this.agent_profile = agent_profile;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public String getVisa() {
        return visa;
    }

    public String getHours() {
        return hours;
    }

    public String getCricos() {
        return cricos;
    }

    public double getDistance() {
        return distance;
    }

    public int getReviews() {
        return reviews;
    }

    public double getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVisa(String visa) {
        this.visa = visa;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setCricos(String cricos) {
        this.cricos = cricos;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
