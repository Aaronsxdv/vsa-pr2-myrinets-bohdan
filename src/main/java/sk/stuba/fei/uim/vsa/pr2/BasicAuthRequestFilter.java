package sk.stuba.fei.uim.vsa.pr2;

import com.sun.javafx.logging.Logger;
import sk.stuba.fei.uim.vsa.pr2.auth.Secured;
import sk.stuba.fei.uim.vsa.pr2.classes.AuthContext;
import sk.stuba.fei.uim.vsa.pr2.solution.Student;
import sk.stuba.fei.uim.vsa.pr2.solution.Teacher;
import sk.stuba.fei.uim.vsa.pr2.solution.ThesisService;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Base64;

@PreMatching
@Provider
    public class BasicAuthRequestFilter implements ContainerRequestFilter {
        public static final String AUTH_CONTEXT_PROPERTY = "authContext";
        @Override
        public void filter(ContainerRequestContext ctx) throws IOException{

            String[] auth = getUsernameAndPasswordFromAuthHeader(ctx.getHeaderString(HttpHeaders.AUTHORIZATION));
            //auth[1] = BCryptService.hash(auth[1]);
            String requestUrl = ctx.getUriInfo().getRequestUri().toString();
            System.out.print(ctx.getMethod());
            System.out.println(requestUrl);
            if(requestUrl.equals("http://localhost:8080/api/")) return;
            if(ctx.getMethod().equals("POST") && (requestUrl.equals("http://localhost:8080/api/students") || requestUrl.equals("http://localhost:8080/api/teachers"))) return;

            if(auth == null || auth[0] == null || auth[1] == null) {
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }

            AuthContext authContext = new AuthContext();
            ThesisService ts = new ThesisService();

            Student student = ts.findStudentByEmailAndPassword(auth[0],auth[1]);
            Teacher teacher = ts.findTeacherByEmailAndPassword(auth[0],auth[1]);
            System.out.println("LINE____________________________");

            if(teacher != null){
                authContext.setUserType("teacher");
                authContext.setUserId(teacher.getAisId());
            }
            else if(student != null){
                authContext.setUserType("student");
                authContext.setUserId(student.getAisId());
            }
            else{
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            ctx.setProperty(AUTH_CONTEXT_PROPERTY, authContext);

        }

    private String[] getUsernameAndPasswordFromAuthHeader(String authHeader) {
        String base64encoded = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64encoded));
        String[] credentials = decoded.split(":");
        String username = credentials[0];
        String password = credentials[1];
        return new String[]{username, password};
    }

}

