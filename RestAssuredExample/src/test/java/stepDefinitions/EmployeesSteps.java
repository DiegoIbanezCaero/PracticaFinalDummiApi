package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import constants.EmployeeEndpoints;
import entities.Employee;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en_scouse.An;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import utils.Request;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;

public class EmployeesSteps {

    Response response;
    @Given("I perform a GET to the employees endpoint")
    public void getEmployees(){
        response = Request.get(EmployeeEndpoints.GET_EMPLOYEES);
    }

    @And("I verify status code {int} is returned")
    public void verifyStatusCode(int statusCode){
        response.then().assertThat().statusCode(statusCode);
    }

    @And("I verify that the body does not have size {int}")
    public void verifyResponseSize(int size){
        response.then().assertThat().body("size()", not(size));
    }


    @And("I verify the following data in the body response")
    public void verifyEmployeeResponseData(DataTable employeeInfo){
        List<String> data = employeeInfo.transpose().asList(String.class);
        response.then().assertThat().body("data.name", Matchers.equalTo(data.get(0)));
        response.then().assertThat().body("data.salary", Matchers.equalTo(data.get(1)));
        response.then().assertThat().body("data.age", Matchers.equalTo(data.get(2)));

    }

    @Given("I perform a Get with a name equal to {string} to the employee endpoint")
    public void getWithNameEmployee(String name){
        response = Request.getWithName(EmployeeEndpoints.GET_EMPLOYEE_NAME,name);
    }

    @And("I verify the following data in the all body response")
    public void verifyBodyEmployeeResponse(DataTable employeeInformation){
        List<String> data = employeeInformation.transpose().asList(String.class);

        response.then().assertThat().body("data.id", Matchers.equalTo(Integer.parseInt(data.get(0))));
        response.then().assertThat().body("data.employee_name", Matchers.equalTo(data.get(1)));
        response.then().assertThat().body("data.employee_salary", Matchers.equalTo(Integer.parseInt(data.get(2))));
        response.then().assertThat().body("data.employee_age", Matchers.equalTo(Integer.parseInt(data.get(3))));
        response.then().assertThat().body("profile_image", Matchers.equalTo(data.get(4)));

    }

    @And("I verify the message that says {string}")
    public void verifyMessageInBody(String message){
        response.then().assertThat().body("message", Matchers.equalTo(message));
    }

    @And("I verify the message that says {string} is not displayed")
    public void verifyMessageInBodyIsnotDisplayed(String message) {
        response.then().assertThat().body("message", not(Matchers.equalTo(message)));
    }

    @And("I perform a PUT to the update endpoint with the following date and id {string}")
    public void putEmployee(String id, DataTable employeeInfo) throws JsonProcessingException {

        List<String> data = employeeInfo.transpose().asList(String.class);

        Employee employee = new Employee();
        employee.setName(data.get(0));
        employee.setSalary(data.get(1));
        employee.setAge(data.get(2));

        System.out.println(employee.getName());
        System.out.println(employee.getSalary());
        System.out.println(employee.getAge());

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(employee);

        response = Request.put(EmployeeEndpoints.PUT_EMPLOYEE, id, payload);

    }

    @Given("I perform a Delete with specific id equal {string} to the employee endpoint")
    public void deleteEmployee(String id){
        response = Request.delete(EmployeeEndpoints.DELETE_EMPLOYEE, id);

    }

    @And("I verify number data deleted equal to {string}")
    public void verifyNumberDataDeleted(String id){
        response.then().assertThat().body("data", Matchers.equalTo(id));
    }

}