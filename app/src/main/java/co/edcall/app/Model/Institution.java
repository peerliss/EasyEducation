package co.edcall.app.Model;

//import android.media.Image;

public class Institution {
    private String name;
    private String location;
    private String description;
    private String coursesAvailable;
    private String hours;
    private String cricos;
    private double distance;
    private int reviews;
    private double rating;
    //    private Image agent_profile;
    private String id;

    public Institution() {
    }

    public Institution(String name, String location, String description, String coursesAvailable, String hours, String cricos, double distance, int reviews, double rating/*, Image agent_profile*/, String id) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.coursesAvailable = coursesAvailable;
        this.hours = hours;
        this.cricos = cricos;
        this.distance = distance;
        this.reviews = reviews;
        this.rating = rating;
//        this.agent_profile = agent_profile;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getCoursesAvailable() {
        return coursesAvailable;
    }

    public String getHours() {
        return hours;
    }

    public String getCricos() {
        return cricos;
    }

    public String getId() {
        return id;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoursesAvailable(String coursesAvailable) {
        this.coursesAvailable = coursesAvailable;
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

    public void setId(String id) {
        this.id = id;
    }
}
