package au.com.easyeducation.easyeducation_3.Model;

//import android.media.Image;

public class Agent {
    private String name;
    private String username;
    private String description;
    private String visa;
    private String hours;
    private double distance;
    private int reviews;
    private double rating;
//    private Image agent_profile;

    public Agent(){}

    public Agent(String name, String username, String description, String visa, String hours, double distance, int reviews, double rating/*, Image agent_profile*/) {
        this.name = name;
        this.username = username;
        this.description = description;
        this.visa = visa;
        this.hours = hours;
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

    public double getDistance() {
        return distance;
    }

    public int getReviews() {
        return reviews;
    }

    public double getRating() {
        return rating;
    }
}
