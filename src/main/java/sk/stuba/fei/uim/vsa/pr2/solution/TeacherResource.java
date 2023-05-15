package sk.stuba.fei.uim.vsa.pr2.solution;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sk.stuba.fei.uim.vsa.pr2.classes.CreateStudentRequest;
import sk.stuba.fei.uim.vsa.pr2.classes.CreateTeacherRequest;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;


@Path("/teachers")
public class TeacherResource {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Context
    private UriInfo context;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTeacher(CreateTeacherRequest request){
        try {

            ThesisService ts = new ThesisService();
            Teacher newTeacher = ts.createTeacher(request.getAisId(),request.getName(),request.getEmail(),request.getDepartment(),request.getPassword());


            return Response.status(Response.Status.CREATED).entity(newTeacher).build();
        } catch (Exception e) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating teacher: " + e.getMessage())
                    .build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllTeachers() throws JsonProcessingException {
        ThesisService ts = new ThesisService();
        List<Teacher> teachers = ts.getTeachers();
        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(teachers);
        return response;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeacher(@PathParam("id") Long id) throws JsonProcessingException {
        try {
            ThesisService ts = new ThesisService();
            Teacher teacher = ts.getTeacher(id);
            return Response.status(Response.Status.OK).entity(teacher).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving teacher: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTeacher(@PathParam("id") Long id) throws JsonProcessingException {
        try {
            ThesisService ts = new ThesisService();
            ts.deleteTeacher(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting teacher: " + e.getMessage())
                    .build();
        }
    }



    private void initResource() {
        ThesisService ts = new ThesisService();
        Teacher anthony = ts.createTeacher(Long.valueOf("304502"),"Anthony","teachermail","code","encoded");
    }
    //public TeacherResource() {initResource();};


}
