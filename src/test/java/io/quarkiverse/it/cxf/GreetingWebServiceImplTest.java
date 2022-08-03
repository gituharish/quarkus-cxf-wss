package io.quarkiverse.it.cxf;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Proxy;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GreetingWebServiceImplTest {

    @Inject
    public GreetingWebService greetingWS;

    @Test
    void testIsNotProxy() {
        Assertions.assertFalse(Proxy.isProxyClass(greetingWS.getClass()));
    }

    @Test
    void testCxfClient() {
        Assertions.assertEquals("Hello bar", greetingWS.reply("bar"));
    }

    @Test
    void testPing() {
        String ret = null;
        try {
            ret = greetingWS.ping("bar");
        } catch (GreetingException e) {
        }
        Assertions.assertEquals("Hello bar", ret);
    }


    @Test
    void testSoapEndpoint() {
//        String xml = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cxf=\"http://cxf.it.quarkiverse.io/\">\n"
//                +
//                "   <x:Header/>\n" +
//                "   <x:Body>\n" +
//                "      <cxf:reply>\n" +
//                "          <text>foo</text>\n" +
//                "      </cxf:reply>\n" +
//                "   </x:Body>\n" +
//                "</x:Envelope>";
        var xml = getXMLString(
                getClass().getClassLoader().getResource("request.xml").getFile());

        given()
                .header("Content-Type", "text/xml").and().body(xml)
                .when().post("/soap/greeting")
                .then()
                .statusCode(200)
                .body(containsString("Hello gitu"));
    }

    @Test
    void testSoap12Binding() {
        given()
                .when().get("/soap/greeting?wsdl")
                .then()
                .statusCode(200)
                .body(containsString("http://schemas.xmlsoap.org/wsdl/soap12/"));
    }

    public static String getXMLString(String xmlFilePath) {
        if (xmlFilePath == null || xmlFilePath.isEmpty()) {
            throw new IllegalArgumentException("xmlFilePath can't be null or empty");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(new File(xmlFilePath)))) {
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }

            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
