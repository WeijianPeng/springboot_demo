package com.china.demo.example.controller.dto.request;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CreateExampleRequest {

    @ApiParam(required = true)
    private String exampleName;


}
