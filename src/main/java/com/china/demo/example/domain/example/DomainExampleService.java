package com.china.demo.example.domain.example;

import com.china.demo.example.domain.exception.BusinessException;
import com.china.demo.example.domain.exception.DomainValidateService;
import com.china.demo.example.domain.exception.ExceptionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomainExampleService {


    @Autowired
    private DomainExampleRepository domainExampleRepository;

    @Autowired
    private DomainValidateService domainValidateService;

    public DomainExample createApiSvcResource(DomainExample domainExample) {
        domainValidateService.validateOrThrowBusinessException(domainExample, ExceptionKey.INVALID_PROPERTY);
        return domainExampleRepository.add(domainExample);
    }

    public DomainExample findApiSvcResource(String serviceId) throws BusinessException {
        return domainExampleRepository.findOneById(serviceId)
                .orElseThrow(() -> new BusinessException(ExceptionKey.NOT_FOUND, "not found resource"));
    }

    public List<DomainExample> findApiSvcResourceList(String svcResourceName) {
        return domainExampleRepository.findAllBySvcName(svcResourceName);
    }


}
