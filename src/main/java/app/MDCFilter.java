package app;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.slf4j.MDC;

public class MDCFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String MDC_KEY_URI = "uri";
    private static final String MDC_KEY_METHOD = "method";

    @Override
    public ContainerRequest filter(final ContainerRequest request) {
        // note to self:
        // using an interceptor to put resource method params into MDC would be also nice
        MDC.put(MDC_KEY_METHOD, request.getMethod());
        MDC.put(MDC_KEY_URI, request.getRequestUri().toASCIIString());
        return request;
    }

    @Override
    public ContainerResponse filter(final ContainerRequest request, final ContainerResponse response) {
        MDC.clear();
        return response;
    }
}
