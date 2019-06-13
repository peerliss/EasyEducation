package co.edcall.app.Model;

import androidx.annotation.Keep;

@Keep
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

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getDob() {
        return dob;
    }
}
