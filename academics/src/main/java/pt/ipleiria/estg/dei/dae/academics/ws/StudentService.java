package pt.ipleiria.estg.dei.dae.academics.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.dae.academics.dtos.EmailDTO;
import pt.ipleiria.estg.dei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.dae.academics.ejbs.EmailBean;
import pt.ipleiria.estg.dei.dae.academics.ejbs.StudentBean;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.dae.academics.security.Authenticated;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Path("students")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Teacher", "Administrator"})
public class StudentService{
    @EJB
    private StudentBean studentBean;
    @EJB
    private EmailBean emailBean;

    private StudentDTO toDTO(Student student){
        return new StudentDTO(
            student.getUsername(),
            student.getPassword(),
            student.getName(),
            student.getEmail(),
            student.getCourse().getCode(),
            student.getCourse().getName()
        );
    }

    private List<StudentDTO> toDTOs(List<Student> students){
        return students.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public List<StudentDTO> getAllStudents(){
        return toDTOs(studentBean.getAll());
    }

    @POST
    @Path("/")
    public Response create(StudentDTO studentDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );
        Student student = studentBean.find(studentDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(StudentDTO.from
                (student)).build();
    }

    @GET
    @Path("/{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @RolesAllowed({"Student"})
    public Response get(@PathParam("username") String username) {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var entity = studentBean.find(username);
        var dto = toDTO(entity);
        return Response.ok(dto).build();
    }

    @GET
    @Path("{username}/subjects")
    public Response getStudentSubjects(@PathParam("username") String username) {
        var student = studentBean.findWithSubjects(username);
        return Response.ok(SubjectDTO.from(student.getSubjects())).build();
    }

    @PUT
    @Path("{username}")
    public Response updateStudent(@PathParam("username") String username, StudentDTO studentDTO) {
        studentBean.update(username, studentDTO.getPassword(), studentDTO.getName(), studentDTO.getEmail(), studentDTO.getCourseCode());
        var student = studentBean.find(username);
        return Response.ok(StudentDTO.from(student)).build();
    }

    @POST
    @Path("/{username}/email")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email)
            throws MyEntityNotFoundException, MessagingException {
        Student student = studentBean.find(username);
        emailBean.send(student.getEmail(), email.getSubject(), email.getBody());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }
}
