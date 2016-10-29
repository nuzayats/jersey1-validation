package app;

import com.sun.jersey.api.ParamException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ParamExceptionMapper implements ExceptionMapper<ParamException> {

    private static final Logger LOGGER = Logger.getLogger(ParamExceptionMapper.class.getName());

    @Override
    public Response toResponse(final ParamException e) {
        LOGGER.log(Level.FINE, "ParamException occurred", e);

        final StringBuilder sb = new StringBuilder("Your parameter '" + e.getParameterName() + "' is invalid");

        final Throwable cause = e.getCause();
        if (cause instanceof IllegalArgumentException) {
            final String message = cause.getMessage();
            if (message != null && !message.isEmpty()) {
                sb.append(": ").append(message);
            }
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(sb.toString())
                .build();
    }
}
