package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("myresource")
@ValidateOnExecution // This enables validation
public class MyResource {

    private static final Logger log = LoggerFactory.getLogger(MyResource.class);

    @Inject
    MyService myService;

    // Use @NotNull for null checking

    @GET
    @Path("emp")
    @Produces(MediaType.TEXT_PLAIN)
    public String emp(@NotNull @QueryParam("id") final EmployeeId id) {
        log.trace("request succeeded");

        return myService.hello(id);
    }

    @GET
    @Path("npe")
    @Produces(MediaType.TEXT_PLAIN)
    public String npe() {
        throw new NullPointerException("This will be processed by RuntimeExceptionMapper");
    }
}
