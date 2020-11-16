package ru.ncedu.frolov.entrantportal.domain;

import javax.persistence.*;

@Entity
@Table(name = "passport_data")
public class PassportData {
    @Id
    private Long id;

    @Column(name = "series")
    private String series;

    @Column(name = "number")
    private String number;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    //JsonIgnore()
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
