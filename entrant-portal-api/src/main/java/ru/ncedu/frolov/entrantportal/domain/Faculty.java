package ru.ncedu.frolov.entrantportal.domain;

import ru.ncedu.frolov.entrantportal.domain.base.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "faculties")
public class Faculty extends AbstractEntity {
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
