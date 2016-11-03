package app;

import com.sun.jersey.api.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ParamExceptionMapper implements ExceptionMapper<ParamException> {

    private static final Logger log = LoggerFactory.getLogger(ParamExceptionMapper.class);

    @Override
    public Response toResponse(final ParamException e) {
        log.debug("ParamException occurred", e);

        final StringBuilder sb = new StringBuilder("Your parameter '" + e.getParameterName() + "' is invalid");

        final Throwable cause = e.getCause();
        if (cause instanceof IllegalArgumentException) {
            final String message = cause.getMessage();
            if (message != null && !message.isEmpty()) {
                sb.append(": ").append(message);
            }
        }

        final String path = RequestURIFilter.getRequestURI();
        log.info("Use this variable to change the response according to request path: {}", path);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(sb.toString())
                .build();
    }
}
