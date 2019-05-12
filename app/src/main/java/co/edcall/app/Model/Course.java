package co.edcall.app.Model;

public class Course {
    private String name;
    private String courseCode;
    private String courseCricos;
    private String intake;
    private String duration;
    private String hours;
    private String availability;
    private int fullFee;
    private int enrolmentFee;
    private int materialFee;
    private int cashback;
    private int payable;
    private int installments;
    private String overview;

    public Course() {
    }

    public Course(String name, String courseCode, String courseCricos, String intake, String duration, String hours, String availability, int fullFee, int enrolmentFee, int materialFee, int cashback, int payable, int installments, String overview) {
        this.name = name;
        this.courseCode = courseCode;
        this.courseCricos = courseCricos;
        this.intake = intake;
        this.duration = duration;
        this.hours = hours;
        this.availability = availability;
        this.fullFee = fullFee;
        this.enrolmentFee = enrolmentFee;
        this.materialFee = materialFee;
        this.cashback = cashback;
        this.payable = payable;
        this.installments = installments;
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

    public String getCourseCricos() {
        return courseCricos;
    }

    public void setCourseCricos(String courseCricos) {
        this.courseCricos = courseCricos;
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

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getFullFee() {
        return fullFee;
    }

    public void setFullFee(int fullFee) {
        this.fullFee = fullFee;
    }

    public int getEnrolmentFee() {
        return enrolmentFee;
    }

    public void setEnrolmentFee(int enrolmentFee) {
        this.enrolmentFee = enrolmentFee;
    }

    public int getMaterialFee() {
        return materialFee;
    }

    public void setMaterialFee(int materialFee) {
        this.materialFee = materialFee;
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

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
