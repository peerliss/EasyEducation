package au.com.easyeducation.easyeducation_3.Model;

public class Course {
    private String name;
    private String courseCode;
    private String intake;
    private String duration;
    private int fullFee;
    private int cashback;
    private int payable;
    private String overview;

    public Course() {}

    public Course(String name, String courseCode, String intake, String duration, int fullFee, int cashback, int payable, String overview) {
        this.name = name;
        this.courseCode = courseCode;
        this.intake = intake;
        this.duration = duration;
        this.fullFee = fullFee;
        this.cashback = cashback;
        this.payable = payable;
        this.overview = overview;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getFullFee() {
        return fullFee;
    }

    public void setFullFee(int fullFee) {
        this.fullFee = fullFee;
    }

    public int getCashback() {
        return cashback;
    }

    public void setCashback(int cashback) {
        this.cashback = cashback;
    }

    public int getPayable() {
        return payable;
    }

    public void setPayable(int payable) {
        this.payable = payable;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
