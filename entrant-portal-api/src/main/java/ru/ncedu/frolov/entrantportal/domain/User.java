package ru.ncedu.frolov.entrantportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.ncedu.frolov.entrantportal.domain.base.AbstractEntity;
import ru.ncedu.frolov.entrantportal.domain.enums.Role;
import ru.ncedu.frolov.entrantportal.domain.enums.UserStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private UserData userData;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private PassportData passportData;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Application> applications;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Education> educations = new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private UserStatus userStatus;

    public void addEducation(Education education) {
        this.educations.add(education);
        education.setUser(this);
    }

    public void addApplication(Application application) {
        this.applications.add(application);
        application.setUser(this);
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public PassportData getPassportData() {
        return passportData;
    }

    public void setPassportData(PassportData passportData) {
        this.passportData = passportData;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Set<Education> getEducations() {
        return educations;
    }

    public void setEducations(Set<Education> educations) {
        this.educations = educations;
    }
}
