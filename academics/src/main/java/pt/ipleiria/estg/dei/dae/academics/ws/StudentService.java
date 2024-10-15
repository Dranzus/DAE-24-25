package pt.ipleiria.estg.dei.dae.academics.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.dae.academics.ejbs.StudentBean;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.dae.academics.entities.Student;

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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewStudent (StudentDTO studentDTO){
        studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );

        Student newStudent = studentBean.find(studentDTO.getUsername());
        return Response.status(Response.Status.CREATED)
                .entity(StudentDTO.from(newStudent))
                .build();
    }

}
