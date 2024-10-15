package pt.ipleiria.estg.dei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.dae.academics.entities.Student;

import java.util.List;

@Stateless
public class StudentBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email, long courseCode) {
        var course = em.find(Course.class, courseCode);
        var student = new Student(username, password, name, email, course);
        em.persist(student);
        course.addStudent(student);
    }

    public List<Student> getAll(){
        return em.createNamedQuery("getAllStudents", Student.class).getResultList();
    }

    public Student find(String username) {
        var student = em.find(Student.class, username);
        if (student == null) {
            throw new RuntimeException("student " + username + " not found");
        }
        return student;
    }
}
