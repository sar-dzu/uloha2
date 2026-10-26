package com.example.uloha2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Meno je povinné")
    @Size(min = 2, max = 50, message = "Meno musí mať 2-50 znakov")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Priezvisko je povinné")
    @Size(min = 2, max = 50, message = "Priezvisko musí mať 2-50 znakov")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @PastOrPresent(message = "Dátum narodenia nemôže byť v budúcnosti")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotBlank(message = "Email je povinný")
    @Pattern(regexp = "^[\\p{L}\\p{N}_%+-]+(?:\\.[\\p{L}\\p{N}_%+-]+)*@(?:[\\p{L}\\p{N}](?:[\\p{L}\\p{N}-]{0,61}[\\p{L}\\p{N}])?\\.)+[\\p{L}\\p{N}](?:[\\p{L}\\p{N}-]{0,61}[\\p{L}\\p{N}])$",
            message = "Neplatný formát emailovej adresy"
    )
    @Size(max = 100, message = "Email môže mať najviac 100 znakov")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "job_title")
    private String jobTitle;

    @PositiveOrZero(message = "Plat nemôže byť záporný")
    @Column(name = "salary")
    private Integer salary;

    @Column(name = "full_time")
    private boolean fullTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public boolean getFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }
}
