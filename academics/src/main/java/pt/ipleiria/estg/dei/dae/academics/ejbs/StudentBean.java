package pt.ipleiria.estg.dei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.dae.academics.entities.Subject;

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

    public Student findWithSubjects(String username){
        var student = this.find(username);
        Hibernate.initialize(student.getSubjects());
        return student;
    }

    public void enrollStudentInSubject(String studentUsername, long subjectCode) {
        var student = em.find(Student.class, studentUsername);
        var subject = em.find(Subject.class, subjectCode);
        student.addSubject(subject);
        subject.addStudent(student);
    }
}
