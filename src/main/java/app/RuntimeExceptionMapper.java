package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger log = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    @Override
    public Response toResponse(final RuntimeException e) {
        // taken from http://stackoverflow.com/questions/13716793/jersey-how-to-register-a-exceptionmapper-that-omits-some-subclasses
        if (e instanceof WebApplicationException) {
            log.trace("WebApplicationException occurred", e);
            return ((WebApplicationException) e).getResponse();
        }

        log.warn("RuntimeException occurred", e);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Sorry, something went wrong")
                .build();
    }
}
