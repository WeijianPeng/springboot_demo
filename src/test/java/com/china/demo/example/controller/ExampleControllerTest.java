package com.china.demo.example.controller;

import com.china.demo.example.controller.dto.request.CreateExampleRequest;
import com.china.demo.example.controller.dto.response.IdResponse;
import com.china.demo.example.controller.exception.ExceptionDTO;
import com.china.demo.example.domain.example.DomainExample;
import com.china.demo.example.domain.exception.ExceptionKey;
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleControllerTest extends DBTestBase {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @ExpectedDatabase(value = "/dbunit/controller/ExampleControllerTest/should_post_example_successful_expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown(value = "/dbunit/controller/ExampleControllerTest/should_post_example_successful_expected.xml",
            type = DatabaseOperation.DELETE_ALL)
    public void should_create_api_resource_successful() {
        CreateExampleRequest request = new CreateExampleRequest();
        request.setExampleName("name1");
        ResponseEntity<IdResponse> idResponse = testRestTemplate.postForEntity("/example", request,
                IdResponse.class);
        assertThat(idResponse.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void should_create_api_resource_failed() {
        CreateExampleRequest request = new CreateExampleRequest();
        ResponseEntity<ExceptionDTO> responseEntity = testRestTemplate
                .postForEntity("/example", request, ExceptionDTO.class);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(responseEntity.getBody().getErrorCode()).isEqualTo(ExceptionKey.INVALID_PROPERTY.toString());
        assertThat(responseEntity.getBody().getErrorDetails())
                .contains(messageSource.getMessage("example.name.not_empty", null, Locale.CHINESE));
    }

    @Test
    @DatabaseSetup(value = "/dbunit/controller/ExampleControllerTest/should_get_example_successful_setup.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "/dbunit/controller/ExampleControllerTest/should_get_example_successful_setup.xml",
            type = DatabaseOperation.DELETE_ALL)
    public void should_get_api_resource_successful() {
        ResponseEntity<DomainExample> responseEntity = testRestTemplate.getForEntity("/example/00001",
                DomainExample.class);
        assertThat(responseEntity.getBody().getName()).isEqualTo("xxxxxxx1");
        responseEntity = testRestTemplate.getForEntity("/example/000022",
                DomainExample.class);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }


    @Test
    @DatabaseSetup(value = "/dbunit/controller/ExampleControllerTest/should_get_example_successful_setup.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "/dbunit/controller/ExampleControllerTest/should_get_example_successful_setup.xml",
            type = DatabaseOperation.DELETE_ALL)
    public void should_get_api_resource_list_successful() {
        ResponseEntity<PageInfo> responseEntity = testRestTemplate.getForEntity("/example?name=xxxxxx&pageNum=2&pageSize=4",
                PageInfo.class);
        assertThat(responseEntity.getBody().getList().size()).isEqualTo(4);
        assertThat(responseEntity.getBody().getStartRow()).isEqualTo(5);
        assertThat(responseEntity.getBody().getTotal()).isEqualTo(9L);
        assertThat(responseEntity.getBody().getPages()).isEqualTo(3);
        assertThat(((Map) responseEntity.getBody().getList().get(0)).get("id")).isEqualTo("00005");
    }
}