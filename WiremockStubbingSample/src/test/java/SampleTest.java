
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.HttpClientFactory;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import wiremock.org.apache.http.client.methods.CloseableHttpResponse;
import wiremock.org.apache.http.client.methods.HttpGet;
import wiremock.org.apache.http.impl.client.CloseableHttpClient;

import java.nio.file.Paths;;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static wiremock.org.hamcrest.MatcherAssert.assertThat;
import static wiremock.org.hamcrest.Matchers.is;

import org.openqa.selenium.WebDriver;

@WireMockTest(httpPort = 3467)
public class SampleTest
{
    CloseableHttpClient client;
    @BeforeEach
    void init() {
        client = HttpClientFactory.createClient();
    }


    private WebDriver driver;
    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
                Paths.get("src/test/resources/chromedriver_win32/chromedriver.exe").toString());
        if (driver == null) {
            driver = new ChromeDriver();
        }
    }
    //@AfterEach
    public void tearDown() {
        if (driver!=null) {
            driver.close();
            driver.quit();
        }
    }


    @Test
    void provides_wiremock_info_as_method_parameter(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        assertNotNull(wmRuntimeInfo);
        assertNotNull(wmRuntimeInfo.getWireMock());

        assertThrows(IllegalStateException.class, wmRuntimeInfo::getHttpsPort); // HTTPS is off by default

        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.register(get("/zodiac_compatibility/result").willReturn(aResponse()
                //.withBodyFile("response.json")
                .withBody("{\"heading\":\"mock\",\"message\":\"server\"}")
        ));
        //HttpGet request = new HttpGet(wmRuntimeInfo.getHttpBaseUrl() + "/zodiac_compatibility/result");
        HttpGet request = new HttpGet("http://localhost:8080/zodiac_compatibility/result");
        try (CloseableHttpResponse response = client.execute(request)) {
            assertThat(response.getStatusLine().getStatusCode(), is(200));
            //EntityUtils.toString(response.getEntity());
        }

        driver.navigate().to("http://localhost:3000/");
        Thread.sleep(2000);
        driver.findElement(By.className("btn-outline-primary")).click();
        System.out.println("");
    }

}
