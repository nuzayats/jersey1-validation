package app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.apache.bval.guice.ValidationModule;

import java.util.HashMap;
import java.util.Map;

public class MyGuiceServletContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule(){
            @Override
            protected void configureServlets() {
                bind(MyResource.class);

                final Map<String, String> params = new HashMap<>();
                params.put("com.sun.jersey.config.property.packages", MyResource.class.getPackage().getName());
                params.put("com.sun.jersey.spi.container.ContainerRequestFilters", MDCFilter.class.getName());

                serve("/app/*").with(GuiceContainer.class, params);

                install(new ValidationModule());
            }
        });
    }
}
