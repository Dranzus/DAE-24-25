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
    @EJB
    private SubjectBean subjectBean = new SubjectBean();
    @EJB
    private TeacherBean teacherBean = new TeacherBean();
    @EJB
    private AdministratorBean administratorBean = new AdministratorBean();

    @PostConstruct
    public void populateDB(){
        courseBean.create(1,"Desenvolvimento de Aplicações Empresariais");
        courseBean.create(2,"Desenvolvimento de Jogos Eletrónicos");
        courseBean.create(3,"Desenvolvimento de Sistemas de Software");
        subjectBean.create(1,"Programação I","1º",1,1);
        subjectBean.create(2,"Programação II","1º",1,1);
        subjectBean.create(3,"Programação III","2º",2,1);
        studentBean.create("dranzus","1234","Joao Vieira","joao@mail.pt",1);
        studentBean.create("carlos88","1234","Carlos Faria","carlos@mail.pt",2);
        studentBean.create("rita99","1234","Rita Marques","rita@mail.pt",2);
        studentBean.enrollStudentInSubject("dranzus",1);
        studentBean.enrollStudentInSubject("dranzus",2);
        studentBean.enrollStudentInSubject("rita99",3);
        teacherBean.create("jose","1234","José Silva","jose@mail.pt");
        teacherBean.create("maria","1234","Maria Santos","maria@mail.pt");
        administratorBean.create("admin","admin","admin","admin@mail.pt");
        administratorBean.create("admin2","admin2","admin2","admin2@mail.pt");
    }
}
