package pt.ipleiria.estg.dei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.dae.academics.dtos.CourseDTO;
import pt.ipleiria.estg.dei.dae.academics.ejbs.CourseBean;
import pt.ipleiria.estg.dei.dae.academics.entities.Course;

import java.util.List;
import java.util.stream.Collectors;

@Path("courses")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class CourseService {
    @EJB
    private CourseBean courseBean;

    private CourseDTO toDTO(Course course){
        return new CourseDTO(
            course.getCode(),
            course.getName()
        );
    }

    private List<CourseDTO> toDTOs(List<Course> courses){
        return courses.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<CourseDTO> getAllCourses(){
        return toDTOs(courseBean.getAll());
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewCourse (CourseDTO courseDTO){
        courseBean.create(
                courseDTO.getCode(),
                courseDTO.getName()
        );

        Course newCourse = courseBean.find(courseDTO.getCode());
        return Response.status(Response.Status.CREATED)
                .entity(CourseDTO.from(newCourse))
                .build();
    }
}
