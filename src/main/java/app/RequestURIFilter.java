package app;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class RequestURIFilter implements Filter {

    private static final ThreadLocal<String> REQUEST_URI = new ThreadLocal<>();

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
        try {
            REQUEST_URI.set(requestURI);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            REQUEST_URI.remove();
        }
    }

    @Override
    public void destroy() {
    }

    static String getRequestURI() {
        return REQUEST_URI.get();
    }
}
