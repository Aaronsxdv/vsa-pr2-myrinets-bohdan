package sk.stuba.fei.uim.vsa.pr2.solution;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class RootResource {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getRoot() {
        return Response.status(Response.Status.OK).build();
    }
}
