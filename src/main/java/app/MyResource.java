package app;

import com.sun.jersey.api.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("myresource")
public class MyResource {

    private static final Logger log = LoggerFactory.getLogger(MyResource.class);

    // According to http://maxenglander.com/2013/01/11/validating-jersey-request.html ...
    //
    // Jersey 1.x does not provide validation of request parameters -
    // it leaves that up to you. Jersey 2.x does provide validation of request parameters.

    @GET
    @Path("emp")
    @Produces(MediaType.TEXT_PLAIN)
    public String emp(@QueryParam("id") final EmployeeId id) {
        if (id == null) {
            throw new ParamException.QueryParamException(null, "id", null);
        }

        log.trace("request succeeded");

        return "Hello, " + id;
    }

    @GET
    @Path("npe")
    @Produces(MediaType.TEXT_PLAIN)
    public String npe() {
        throw new NullPointerException("This will be processed by RuntimeExceptionMapper");
    }
}
