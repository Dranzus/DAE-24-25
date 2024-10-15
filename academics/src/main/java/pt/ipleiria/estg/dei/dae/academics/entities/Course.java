package pt.ipleiria.estg.dei.dae.academics.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries(
        {
            @NamedQuery(
                    name = "getAllCourses",
                    query = "Select c FROM Course c ORDER BY c.name"
            )
        }
)
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
public class Course implements Serializable {
    @Id
    private long code;
    private String name;
    @OneToMany(mappedBy = "course")
    private List<Student> students;

    public Course() {
        this.students = new ArrayList<>();
    }

    public Course(long code, String name) {
        this.code = code;
        this.name = name;
        this.students = new ArrayList<>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }
}