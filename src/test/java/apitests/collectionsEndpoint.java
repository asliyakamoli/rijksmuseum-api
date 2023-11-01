package apitests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.rijksmuseum.utilities.ConfigurationReader;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;
import static io.restassured.RestAssured.baseURI;


public class collectionsEndpoint {

    @BeforeClass
    public void beforeclass(){
        baseURI = ConfigurationReader.get("api_url");
    }

    @Test
    public void test1(){
        Response response = given().accept(ContentType.JSON)
                .param("key", "0fiuZFh4")
                .when().get();

        //verify status code
        assertEquals(response.statusCode(),200);
    }
}
