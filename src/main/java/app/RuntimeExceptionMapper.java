package app;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger LOGGER = Logger.getLogger(RuntimeExceptionMapper.class.getName());

    @Override
    public Response toResponse(final RuntimeException e) {
        // taken from http://stackoverflow.com/questions/13716793/jersey-how-to-register-a-exceptionmapper-that-omits-some-subclasses
        if (e instanceof WebApplicationException) {
            return ((WebApplicationException) e).getResponse();
        }

        LOGGER.log(Level.WARNING, "RuntimeException occurred", e);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Sorry, something went wrong")
                .build();
    }
}
