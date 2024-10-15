package pt.ipleiria.estg.dei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.dae.academics.entities.Subject;

import java.util.List;

@Stateless
public class SubjectBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long code, String name, String schoolYear, int courseYear, long courseCode) {
        var course = em.find(Course.class, courseCode);
        var subject = new Subject(code, name, schoolYear, courseYear, course);
        em.persist(subject);
        course.addSubject(subject);
    }

    public Subject find(long code) {
        var subject = em.find(Subject.class, code);
        if (subject == null) {
            throw new RuntimeException("subject " + code + " not found");
        }
        return subject;
    }

    public List<Subject> findAll() {
        return em.createNamedQuery("getAllSubjects", Subject.class).getResultList();
    }
}
