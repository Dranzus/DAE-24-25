package pt.ipleiria.estg.dei.dae.academics.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.dae.academics.ejbs.StudentBean;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.dae.academics.exceptions.MyEntityNotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Path("students")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class StudentService{
    @EJB
    private StudentBean studentBean;

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
    @Path("{username}")
    public Response getStudent(@PathParam("username") String username) {
        var student = studentBean.findWithSubjects(username);
        var studentDTO = StudentDTO.from(student);
        studentDTO.setSubjects(SubjectDTO.from(student.getSubjects()));
        return Response.ok(studentDTO).build();
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
}
