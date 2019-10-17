package arkpas.userdatabase.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private int phoneNumber;
    @Transient
    private int age;

    public User() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void calculateAge (LocalDate currentDate) {

        if (this.birthDate != null) {
            int age = currentDate.getYear() - this.birthDate.getYear();

            //check if user had birthday this year, if not we have to substract one year from his age, because his birthday is after present day
            LocalDate helperDate = this.birthDate.plusYears(age);
            if (helperDate.isAfter(currentDate))
                this.age = age - 1;
            else
                this.age = age;
        }
    }

    public String toString() {
        this.calculateAge(LocalDate.now());
        return String.format("%s %s, %s (wiek: %d), %s", this.name, this.surname, this.birthDate, this.age, this.phoneNumber);
    }
}
