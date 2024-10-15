package pt.ipleiria.estg.dei.dae.academics.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class ConfigBean {
    @EJB
    private StudentBean studentBean = new StudentBean();
    @EJB
    private CourseBean courseBean = new CourseBean();

    @PostConstruct
    public void populateDB(){
        courseBean.create(1,"Desenvolvimento de Aplicações Empresariais");
        courseBean.create(2,"Desenvolvimento de Jogos Eletrónicos");
        courseBean.create(3,"Desenvolvimento de Sistemas de Software");
        studentBean.create("dranzus","1234","Joao Vieira","joao@mail.pt",1);
        studentBean.create("carlos88","1234","Carlos Faria","carlos@mail.pt",2);
        studentBean.create("rita99","1234","Rita Marques","rita@mail.pt",2);
    }
}
