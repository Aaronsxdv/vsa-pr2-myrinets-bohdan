package sk.stuba.fei.uim.vsa.pr2.solution;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sk.stuba.fei.uim.vsa.pr2.BasicAuthRequestFilter;
import sk.stuba.fei.uim.vsa.pr2.classes.*;

import java.util.Base64;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.persistence.TypedQuery;


@Path("/theses")
public class ThesisResource {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Context
    private UriInfo context;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createThesis(@Context ContainerRequestContext ctx, CreateThesisRequest request) {
        AuthContext authContext = (AuthContext) ctx.getProperty(BasicAuthRequestFilter.AUTH_CONTEXT_PROPERTY);
        ThesisService ts = new ThesisService();
        try {
            if(authContext.getUserType().equals("teacher")){
                Teacher supervisor = ts.getTeacher(authContext.getUserId());
                Thesis newThesis = ts.makeThesisAssignment(supervisor.getAisId(),request.getTitle(),request.getType().name(),request.getDescription());
                return Response.status(Response.Status.CREATED).entity(newThesis).build();
            }
            else{
                Message errorMessage = new Message(401, "Authentication error", "Error Type", "");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(errorMessage)
                        .build();
            }
        } catch (Exception e) {

            Message errorMessage = new Message(500, "Error creating thesis", "Error Type", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorMessage)
                    .build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTheses(@Context ContainerRequestContext ctx) throws JsonProcessingException {
        AuthContext authContext = (AuthContext) ctx.getProperty(BasicAuthRequestFilter.AUTH_CONTEXT_PROPERTY);
        ThesisService ts = new ThesisService();
        if(authContext.getUserType().equals("student") || authContext.getUserType().equals("teacher")){
            List<Thesis> theses = ts.getTheses();
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(theses);
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

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getThesisById(@Context ContainerRequestContext ctx,@PathParam("id") Long id) throws JsonProcessingException {
        AuthContext authContext = (AuthContext) ctx.getProperty(BasicAuthRequestFilter.AUTH_CONTEXT_PROPERTY);
        try {
            if(authContext.getUserType().equals("student") || authContext.getUserType().equals("teacher")){

                ThesisService ts = new ThesisService();
                Thesis thesis = ts.getThesis(id);
                if(thesis == null){
                    Message errorMessage = new Message(404, "Requested resource was not found", "Thesis", "");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(errorMessage)
                            .build();
                }
                return Response.status(Response.Status.OK).entity(thesis).build();
            }
            else{
                Message errorMessage = new Message(401, "Unauthorized", "Error Type", "");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(errorMessage)
                        .build();
            }


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
    public Response deleteThesis(@Context ContainerRequestContext ctx,@PathParam("id") Long id) throws JsonProcessingException {
        AuthContext authContext = (AuthContext) ctx.getProperty(BasicAuthRequestFilter.AUTH_CONTEXT_PROPERTY);
        try {
            if(authContext.getUserType().equals("teacher")){

                ThesisService ts = new ThesisService();
                Thesis thesis = ts.getThesis(id);
                if(thesis == null){
                    Message errorMessage = new Message(404, "Requested resource was not found", "Thesis", "");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(errorMessage)
                            .build();
                }
                if(Objects.equals(authContext.getUserId(), thesis.getSupervisor().getAisId())){
                    ts.deleteThesis(id);
                }

                return Response.status(Response.Status.OK).entity(thesis).build();
            }
            else{
                Message errorMessage = new Message(401, "Unauthorized", "Error Type", "");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(errorMessage)
                        .build();
            }


        } catch (Exception e) {
            Message errorMessage = new Message(500, "Error getting student", "Type", e.getStackTrace().toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorMessage)
                    .build();
        }
    }

    @POST
    @Path("/{id}/assign")
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignThesisToStudent(@Context ContainerRequestContext ctx,@PathParam("id") Long id,SearchThesisRequest request) throws JsonProcessingException {
        AuthContext authContext = (AuthContext) ctx.getProperty(BasicAuthRequestFilter.AUTH_CONTEXT_PROPERTY);
        try {
            if(authContext.getUserType().equals("teacher")){

                ThesisService ts = new ThesisService();
                Thesis thesis = ts.getThesis(id);
                if(thesis != null){
                    ts.assignThesis(id, (long) request.getStudentId());
                }
                if(thesis == null){
                    Message errorMessage = new Message(404, "Requested resource was not found", "Thesis", "");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(errorMessage)
                            .build();
                }

                return Response.status(Response.Status.OK).entity(thesis).build();
            }
            else if(authContext.getUserType().equals("student")){
                ThesisService ts = new ThesisService();
                Thesis thesis = ts.getThesis(id);
                if(thesis != null){
                    ts.assignThesis(id,authContext.getUserId());
                }


            }
            else{
                Message errorMessage = new Message(401, "Unauthorized", "Error Type", "");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(errorMessage)
                        .build();
            }


        } catch (Exception e) {
            Message errorMessage = new Message(500, "Error getting student", "Type", e.getStackTrace().toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorMessage)
                    .build();
        }
        return null;
    }

    @POST
    @Path("/search/theses")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchTheses(@Context ContainerRequestContext ctx,SearchThesisRequest request) throws JsonProcessingException {
        AuthContext authContext = (AuthContext) ctx.getProperty(BasicAuthRequestFilter.AUTH_CONTEXT_PROPERTY);
        try {
            List<Thesis> theses = new ArrayList<>();
            if(authContext.getUserType().equals("student") || authContext.getUserType().equals("teacher")){

                ThesisService ts = new ThesisService();
                if(!Objects.equals(request.getStudentId(), null)){
                    Thesis studentThesis = ts.getThesisByStudent((long) request.getStudentId());
                    theses.add(studentThesis);
                }
                else if (!Objects.equals(request.getTeacherId(), null)){
                    List<Thesis> teacherTheses = ts.getThesesByTeacher((long) request.getTeacherId());
                    theses.addAll(teacherTheses);
                }


                return Response.status(Response.Status.OK).entity(theses).build();
            }
            else{
                Message errorMessage = new Message(401, "Unauthorized", "Error Type", "");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(errorMessage)
                        .build();
            }


        } catch (Exception e) {
            Message errorMessage = new Message(500, "Error getting student", "Type", e.getStackTrace().toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorMessage)
                    .build();
        }
    }



    private void initResource() {
        ThesisService ts = new ThesisService();
        Student bohdan = ts.createStudent(Long.valueOf("105304"),"Bohdan","mail","pass");
    }
    //public ThesisResource() {initResource();};
}