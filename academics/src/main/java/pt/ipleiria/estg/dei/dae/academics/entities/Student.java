package pt.ipleiria.estg.dei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries(
        {
            @NamedQuery(
                    name = "getAllStudents",
                    query = "Select s FROM Student s ORDER BY s.name"
            )
        }
)
public class Student extends User implements Serializable {
    @NotNull
    @ManyToOne
    private Course course;
    @ManyToMany (mappedBy = "students")
    private List<Subject> subjects;


    public Student() {
        this.subjects = new ArrayList<>();
    }

    public Student(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
        this.course = course;
        this.subjects = new ArrayList<>();
    }

    public @NotNull Course getCourse() {
        return course;
    }

    public void setCourse(@NotNull Course course) {
        this.course = course;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        this.subjects.remove(subject);
    }
}
