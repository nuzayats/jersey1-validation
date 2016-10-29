package app;

import com.sun.jersey.api.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    private static final Logger log = LoggerFactory.getLogger(NotFoundExceptionMapper.class);

    @Override
    public Response toResponse(final NotFoundException e) {
        log.debug("NotFoundException occurred", e);

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Check the destination path of your request - we have no API here")
                .build();
    }
}
