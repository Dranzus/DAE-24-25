package pt.ipleiria.estg.dei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.dae.academics.entities.Course;

import java.util.List;

@Stateless
public class CourseBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long code, String name) {
        var course = new Course(code, name);
        em.persist(course);
    }

    public List<Course> getAll(){
        return em.createNamedQuery("getAllCourses", Course.class).getResultList();
    }

    public Course find(long code) {
        var course = em.find(Course.class, code);
        if (course == null) {
            throw new RuntimeException("course " + code + " not found");
        }
            return course;
    }
}
