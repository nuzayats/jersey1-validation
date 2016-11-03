package app;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(Arquillian.class)
public class MyResourceIT {
    private static final Logger log = LoggerFactory.getLogger(MyResourceIT.class);

    private static CloseableHttpClient client;
    private CloseableHttpResponse response;

    @ArquillianResource
    private URL baseUrl;
    private String myResourcePath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        final WebArchive webArchive = ShrinkWrap
                .create(WebArchive.class, "ROOT.war")
//                .addPackage(MyResource.class.getPackage())
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"))
//                .addAsLibraries(
//                        Maven.resolver()
//                                .loadPomFromFile("pom.xml")
//                                .resolve("com.sun.jersey:jersey-server", "com.sun.jersey:jersey-servlet")
//                                .withTransitivity()
//                                .asFile())
                ;
        return webArchive;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        client = HttpClients.createDefault();
    }

    @Before
    public void setUp() throws Exception {
        myResourcePath = baseUrl.toURI() + "/app/myresource";
    }

    // ------------------------------------------------------------------------

    @Test
    public void empReturns200WhenIdIsValid() throws Exception {
        final long validId = 123;
        final String path = createPath("/emp/?id=" + validId);

        response = client.execute(new HttpGet(path));

        final String content = getContent(response);
        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(content, is("Hello, 123"));
    }

    @Test
    public void empReturns400WhenIdIsNotSupplied() throws Exception {
        final String path = createPath("/emp");

        response = client.execute(new HttpGet(path));

        final String content = getContent(response);
        assertThat(response.getStatusLine().getStatusCode(), is(400));
        assertThat(content, startsWith("Your parameter 'id' is invalid"));
    }

    @Test
    public void empReturns400WhenIdIsNegative() throws Exception {
        final long negativeId = -1;
        final String path = createPath("/emp/?id=" + negativeId);

        response = client.execute(new HttpGet(path));

        final String content = getContent(response);
        assertThat(response.getStatusLine().getStatusCode(), is(400));
        assertThat(content, is("Your parameter 'id' is invalid: EmployeeId must be larger than zero"));
    }

    @Test
    public void empReturns400WhenIdIsNotANumber() throws Exception {
        final String notANumber = "foo";
        final String path = createPath("/emp/?id=" + notANumber);

        response = client.execute(new HttpGet(path));

        final String content = getContent(response);
        assertThat(response.getStatusLine().getStatusCode(), is(400));
        assertThat(content, startsWith("Your parameter 'id' is invalid: For input string: "));
    }

    @Test
    public void empReturns400WhenIdIsTooLarge() throws Exception {
        final String largeNumber = Long.toString(Long.MAX_VALUE) + "000";
        final String path = createPath("/emp/?id=" + largeNumber);

        response = client.execute(new HttpGet(path));

        final String content = getContent(response);
        assertThat(response.getStatusLine().getStatusCode(), is(400));
        assertThat(content, startsWith("Your parameter 'id' is invalid: For input string: "));
    }

    @Test
    public void npeReturns500WithCustomContent() throws Exception {
        final String path = createPath("/npe");

        response = client.execute(new HttpGet(path));

        final String content = getContent(response);
        assertThat(response.getStatusLine().getStatusCode(), is(500));
        assertThat(content, is("Sorry, something went wrong"));
    }

    @Test
    public void wrongPathReturns404WithCustomContent() throws Exception {
        final String path = createPath("/wrong_path");

        response = client.execute(new HttpGet(path));

        final String content = getContent(response);
        assertThat(response.getStatusLine().getStatusCode(), is(404));
        assertThat(content, is("Check the destination path of your request - we have no API here"));
    }

    // ------------------------------------------------------------------------

    @After
    public void tearDown() throws Exception {
        response.close();
    }

    @AfterClass
    public static void afterClass() throws IOException {
        client.close();
    }

    private String createPath(final String s) {
        return myResourcePath + s;
    }

    private static String getContent(final CloseableHttpResponse response) throws IOException {
        final String content;
        try (final InputStream is = response.getEntity().getContent()) {
            content = read(is);
        }
        log.info("{}", response.getStatusLine());
        log.info(content);
        return content;
    }

    // taken from http://www.adam-bien.com/roller/abien/entry/reading_inputstream_into_string_with
    private static String read(final InputStream input) throws IOException {
        try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}
