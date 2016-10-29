package app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("myresource")
public class MyResource {

    // According to http://maxenglander.com/2013/01/11/validating-jersey-request.html ...
    //
    // Jersey 1.x does not provide validation of request parameters -
    // it leaves that up to you. Jersey 2.x does provide validation of request parameters.

    @GET
    @Path("emp")
    @Produces(MediaType.TEXT_PLAIN)
    public String emp(@QueryParam("id") final EmployeeId id) {
        if (id == null) {
            throw new WebApplicationException(
                    Response.status(BAD_REQUEST)
                            .entity("id is required")
                            .build());
        }

        return "Hello, " + id;
    }

    @GET
    @Path("npe")
    @Produces(MediaType.TEXT_PLAIN)
    public String npe() {
        throw new NullPointerException("This will be processed by RuntimeExceptionMapper");
    }
}
