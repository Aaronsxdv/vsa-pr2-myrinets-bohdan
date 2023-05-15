package sk.stuba.fei.uim.vsa.pr2.solution;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sk.stuba.fei.uim.vsa.pr2.BasicAuthRequestFilter;
import sk.stuba.fei.uim.vsa.pr2.classes.AuthContext;
import sk.stuba.fei.uim.vsa.pr2.classes.CreateStudentRequest;
import sk.stuba.fei.uim.vsa.pr2.classes.CreateTeacherRequest;
import sk.stuba.fei.uim.vsa.pr2.classes.Message;

import java.util.Base64;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import javax.persistence.TypedQuery;


@Path("/students")
public class StudentResource {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Context
    private UriInfo context;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createStudent(CreateStudentRequest request) {
        try {

            ThesisService ts = new ThesisService();
            Student newStudent = ts.createStudent(request.getAisId(), request.getName(), request.getEmail(), request.getPassword());
            return Response.status(Response.Status.CREATED).entity(newStudent).build();
        } catch (Exception e) {

            Message errorMessage = new Message(500, "Error creating student", "Error Type", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorMessage)
                    .build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStudents(@Context ContainerRequestContext ctx) throws JsonProcessingException {
        AuthContext authContext = (AuthContext) ctx.getProperty(BasicAuthRequestFilter.AUTH_CONTEXT_PROPERTY);
        ThesisService ts = new ThesisService();
        if(authContext.getUserType().equals("student") || authContext.getUserType().equals("teacher")){
            List<Student> students = ts.getStudents();
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(students);
            return Response.status(Response.Status.OK)
                    .entity(response)
                    .build();
        }
        else{
            Message errorMessage = new Message(401, "Unauthorized", "Error Type", "");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(errorMessage)
                    .build();
        }

    }

    private String getPassword(String authHeader){
        String base64encoded = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64encoded));
        return decoded.split(":")[1];
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") Long id) throws JsonProcessingException {
        try {
            ThesisService ts = new ThesisService();
            Student student = ts.getStudent(id);
            if(student == null){
                Message errorMessage = new Message(404, "Requested resource was not found", "Type", "");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(errorMessage)
                        .build();
            }
            return Response.status(Response.Status.OK).entity(student).build();
        } catch (Exception e) {
            Message errorMessage = new Message(500, "Error getting student", "Type", e.getStackTrace().toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorMessage)
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStudent(@PathParam("id") Long id) throws JsonProcessingException {
        try {
            ThesisService ts = new ThesisService();
            ts.deleteStudent(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting student: " + e.getMessage())
                    .build();
        }
    }

    private void initResource() {
        ThesisService ts = new ThesisService();
        Student bohdan = ts.createStudent(Long.valueOf("105304"),"Bohdan","mail","pass");
    }
    //public StudentResource() {initResource();};
}