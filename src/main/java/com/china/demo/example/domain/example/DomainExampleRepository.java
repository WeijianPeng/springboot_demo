package com.china.demo.example.domain.example;


import java.util.List;
import java.util.Optional;

public interface DomainExampleRepository {

    //return resource after added
    DomainExample add(DomainExample domainExample);

    Optional<DomainExample> findOneById(String apiServiceId);

    List<DomainExample> findAllBySvcName(String apiSvcResourceName);

}
