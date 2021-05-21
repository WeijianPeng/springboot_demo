package com.china.demo.example.controller;

import com.china.demo.example.application.ExampleApp;
import com.china.demo.example.controller.dto.request.CreateExampleRequest;
import com.china.demo.example.controller.dto.response.IdResponse;
import com.china.demo.example.domain.example.DomainExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/example")
public class ExampleController {

    @Autowired
    private ExampleApp exampleApp;

    @PostMapping
    public IdResponse postExampleResource(@Valid @RequestBody CreateExampleRequest createExampleRequest) {

        DomainExample domainExample = new DomainExample()
                .setId(UUID.randomUUID().toString())
                .setName(createExampleRequest.getExampleName());

        domainExample = exampleApp.createExampleResource(domainExample);

        return IdResponse.builder().id(domainExample.getId()).build();
    }

    @GetMapping("/{id}")
    public DomainExample getExampleResourceById(@PathVariable String id) {
        return exampleApp.findExampleResource(id);
    }

    @GetMapping
    public PageInfo<DomainExample> queryExampleResources(@RequestParam("name") String name,
                                                            @RequestParam("pageNum") int pageNum,
                                                            @RequestParam("pageSize") int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DomainExample> list = exampleApp.findExampleResourceList(name);
        return PageInfo.of(list);
    }
}
