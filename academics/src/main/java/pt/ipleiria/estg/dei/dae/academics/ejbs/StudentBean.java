package pt.ipleiria.estg.dei.dae.academics.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.dae.academics.security.Hasher;

import java.util.List;

@Stateless
public class StudentBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    CourseBean courseBean = new CourseBean();

    private Hasher hasher = new Hasher();

    public void create(String username, String password, String name, String email, long courseCode) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        if(exists(username)){
            throw new MyEntityExistsException("Student with username " + username + " already exists");
        }

        var course = courseBean.find(courseCode);
        if (course == null) {
            throw new MyEntityNotFoundException("Course with code " + courseCode + " not found");
        }

        try {
            var student = new Student(username, hasher.hash(password), name, email, course);
            course.addStudent(student);
            em.persist(student);
            em.flush();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(String username, String password, String name, String email,
                       long courseCode) {
        Student student = em.find(Student.class, username);
        if (student == null) {
            System.err.println("ERROR_STUDENT_NOT_FOUND: " + username);
            return;
        }
        em.lock(student, LockModeType.OPTIMISTIC);
        student.setPassword(hasher.hash(password));
        student.setName(name);
        student.setEmail(email);

        if (student.getCourse().getCode() != courseCode) {
            Course course = em.find(Course.class, courseCode);
            if (course == null) {
                System.err.println("ERROR_COURSE_NOT_FOUND: " + courseCode);
                return;
            }
            student.setCourse(course);
        }
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

    public boolean exists(String username) {
        Query query = em.createQuery(
                "SELECT COUNT(s.username) FROM Student s WHERE s.username = :username",
                Long.class
        );
        query.setParameter("username", username);
        return (Long)query.getSingleResult() > 0L;
    }
}
