package app;

import com.sun.jersey.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    private static final Logger LOGGER = Logger.getLogger(NotFoundExceptionMapper.class.getName());

    @Override
    public Response toResponse(final NotFoundException e) {
        LOGGER.log(Level.FINE, "NotFoundException occurred", e);

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Check the destination path of your request - we have no API here")
                .build();
    }
}
