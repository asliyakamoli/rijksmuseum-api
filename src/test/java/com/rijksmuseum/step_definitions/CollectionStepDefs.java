package com.rijksmuseum.step_definitions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import com.rijksmuseum.utilities.ConfigurationReader;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CollectionStepDefs {

    String key;
    Response response;
    private List<Map<String, Object>> artObjects;
    @Given("I set up Rijksmuseum api")
    public void i_set_up_rijksmuseum_api() {
        key = "0fiuZFh4";
    }

    @When("I send a request")
    public void i_send_a_request() {
        //send get request to retrieve current user information
        String url = ConfigurationReader.get("api_url");

        response=     given().accept(ContentType.JSON)
                .param("key", "0fiuZFh4")
                .when()
                .get(url);
    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {

        Assert.assertEquals(statusCode,response.statusCode());

    }

    @When("I check the data types of art object properties")
    public void i_check_data_types_of_art_object_properties() {
        // Extract the art objects from the response
        artObjects = response.jsonPath().getList("artObjects");
    }

    @Then("the {string} should have data type {string}")
    public void property_should_have_data_type(String property, String expectedType) {
        for (Map<String, ?> artObject : artObjects) {
            Object propertyValue = artObject.get(property);

            switch (expectedType) {
                case "string":
                    Assert.assertTrue(propertyValue instanceof String);
                    break;
                case "boolean":
                    Assert.assertTrue(propertyValue instanceof Boolean);
                    break;
                case "integer":
                    Assert.assertTrue(propertyValue instanceof Integer);
                    break;
                case "float":
                    Assert.assertTrue(propertyValue instanceof Float);
                    break;
                case "double":
                    Assert.assertTrue(propertyValue instanceof Double);
                    break;
                // Add more data types as needed
                default:
                    Assert.fail("Unknown data type: " + expectedType);
            }
        }
    }


}
