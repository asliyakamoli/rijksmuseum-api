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
import java.util.regex.Pattern;

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

    @Then("the {string} should not be null")
    public void property_should_not_be_null(String property) {
        for (Map<String, ?> artObject : artObjects) {
            Object propertyValue = artObject.get(property);
            Assert.assertNotNull(propertyValue);
        }
    }

    @Then("the {string} should match the URL format")
    public void property_should_match_url_format(String property) {
        String urlRegex = "^(http|https)://[a-zA-Z0-9-.]+\\.[a-zA-Z]{2,}(/\\S*)?$";
        Pattern pattern = Pattern.compile(urlRegex);

        for (Map<String, ?> artObject : artObjects) {
            Object propertyValue = artObject.get(property);

            // Ensure that the property value is a string
            if (propertyValue instanceof String) {
                String url = (String) propertyValue;
                Assert.assertTrue("URL format is invalid for property: " + property, pattern.matcher(url).matches());
            } else {
                Assert.fail("Property value is not a string: " + property);
            }
        }
    }

    @Then("all {string} should not be null")
    public void all_properties_should_not_be_null(String property) {
        for (Map<String, ?> artObject : artObjects) {
            Object propertyValue = artObject.get(property);
            Assert.assertNotNull(propertyValue);
        }
    }

    @Then("the count should be equal to the count of art objects")
    public void the_count_should_be_equal_to_art_object_count() {
        int expectedCount = response.jsonPath().get("count");
        Assert.assertEquals(expectedCount, artObjects.size());
    }

    @Then("the count of art objects with {string} as true should be equal to {string}")
    public void count_of_art_objects_with_has_image_should_be_equal_to_count(String hasImageProperty, String facetCountProperty) {
        int expectedCount = response.jsonPath().get("countFacets." + facetCountProperty);
        long actualCount = artObjects.stream()
                .filter(artObject -> (boolean) artObject.get(hasImageProperty))
                .count();
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Then("the {string} should contain all parts except {string}")
    public void property_should_contain_all_parts_except(String property, String partToExclude) {
        for (Map<String, ?> artObject : artObjects) {
            Object propertyValue = artObject.get(property);

            if (propertyValue instanceof String) {
                String objectNumber = (String) propertyValue;
                // Split the objectNumber using "-" as a delimiter
                String[] parts = objectNumber.split("-");

                // Check that the parts do not contain the partToExclude
                for (String part : parts) {
                    if (part.equals(partToExclude)) {
                        Assert.fail("The property " + property + " contains the part to exclude: " + partToExclude);
                    }
                }
            } else {
                Assert.fail("Property value is not a string: " + property);
            }
        }
    }

}
