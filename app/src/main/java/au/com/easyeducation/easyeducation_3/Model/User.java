package au.com.easyeducation.easyeducation_3.Model;

public class User {
    public String name, surname, fullname, email, number, dob;

    public User() {

    }

    public User(String name, String surname, String fullname, String email, String number, String dob) {
        this.name = name;
        this.surname = surname;
        this.fullname = fullname;
        this.email = email;
        this.number = number;
        this.dob = dob;
    }
}
