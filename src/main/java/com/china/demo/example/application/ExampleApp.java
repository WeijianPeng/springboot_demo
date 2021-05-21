package com.china.demo.example.application;

import com.china.demo.example.domain.example.DomainExample;
import com.china.demo.example.domain.example.DomainExampleService;
import com.china.demo.example.domain.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ExampleApp {


    @Autowired
    private DomainExampleService domainExampleService;

    @Transactional(timeout = 60)
    public DomainExample createExampleResource(DomainExample domainExample) {
        return domainExampleService.createApiSvcResource(domainExample);
    }

    public DomainExample findExampleResource(String serviceId) throws BusinessException {
        return domainExampleService.findApiSvcResource(serviceId);
    }

    public List<DomainExample> findExampleResourceList(String svcName) {
        return domainExampleService.findApiSvcResourceList(svcName);
    }
}
