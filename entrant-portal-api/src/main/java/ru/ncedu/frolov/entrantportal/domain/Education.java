package ru.ncedu.frolov.entrantportal.domain;

import ru.ncedu.frolov.entrantportal.domain.base.AbstractEntity;
import ru.ncedu.frolov.entrantportal.domain.enums.Grade;

import javax.persistence.*;

@Entity
@Table(name = "educations")
public class Education extends AbstractEntity {
    @Enumerated(value = EnumType.STRING)
    private Grade grade;

    @Column(name = "document_path")
    private String documentPath;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
