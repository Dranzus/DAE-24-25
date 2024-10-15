package pt.ipleiria.estg.dei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.dae.academics.entities.Administrator;

@Stateless
public class AdministratorBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String name, String email) {
        var administrator = new Administrator(username, password, name, email);
        em.persist(administrator);
    }
}
