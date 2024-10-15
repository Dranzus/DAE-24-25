package pt.ipleiria.estg.dei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.dae.academics.entities.Teacher;

@Stateless
public class TeacherBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email) {
        var teacher = new Teacher(username, password, name, email);
        em.persist(teacher);
    }
}
