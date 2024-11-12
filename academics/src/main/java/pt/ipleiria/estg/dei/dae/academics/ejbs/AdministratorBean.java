package pt.ipleiria.estg.dei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.dae.academics.entities.Administrator;
import pt.ipleiria.estg.dei.dae.academics.security.Hasher;

@Stateless
public class AdministratorBean {
    @PersistenceContext
    private EntityManager em;

    private Hasher hasher = new Hasher();

    public void create(String username, String password, String name, String email) {
        var administrator = new Administrator(username, hasher.hash(password), name, email);
        em.persist(administrator);
    }
}
