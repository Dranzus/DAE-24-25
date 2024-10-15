package pt.ipleiria.estg.dei.dae.academics.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.io.Serializable;
import java.util.List;

@Entity
public class Teacher extends User implements Serializable {

    private String office;
    @ManyToMany(mappedBy = "teachers")
    private List<Subject> subjects;

    public Teacher() {
    }

    public Teacher(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}
