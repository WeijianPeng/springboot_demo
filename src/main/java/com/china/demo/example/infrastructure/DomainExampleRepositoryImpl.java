package com.china.demo.example.infrastructure;


import com.china.demo.example.domain.example.DomainExample;
import com.china.demo.example.domain.example.DomainExampleRepository;
import com.china.demo.example.infrastructure.persistence.AbstractRepositoryImpl;
import com.china.demo.example.infrastructure.persistence.mybatis.example.ExampleDBO;
import com.china.demo.example.infrastructure.persistence.mybatis.example.ExampleDBOMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;

@Repository
public class DomainExampleRepositoryImpl extends AbstractRepositoryImpl implements DomainExampleRepository {

    @Autowired
    private ExampleDBOMapper exampleDBOMapper;

    @Override
    public DomainExample add(DomainExample domainExample) {
        exampleDBOMapper.insert(toDBO(domainExample));
        return domainExample;
    }

    @Override
    public Optional<DomainExample> findOneById(String apiServiceId) {
        ExampleDBO exampleDBO = exampleDBOMapper.selectByPrimaryKey(apiServiceId);
        if (null == exampleDBO) {
            return Optional.empty();
        }
        return Optional.of(toDomainModel(exampleDBO));
    }

    @Override
    public List<DomainExample> findAllBySvcName(String svcResourceName) {
        Example example = new Example(ExampleDBO.class);
        example.createCriteria().andLike("name", svcResourceName + "%" );
        List<ExampleDBO> dboList = exampleDBOMapper.selectByExample(example);
        return convertDBOListToDomainList(dboList);

    }


    protected ExampleDBO toDBO(Object domainModel) {
        DomainExample domainExample = (DomainExample) domainModel;
        ExampleDBO result = new ExampleDBO();
        BeanUtils.copyProperties(domainExample, result);
        return result;
    }

    protected DomainExample toDomainModel(Object dbo) {
        ExampleDBO exampleDBO = (ExampleDBO) dbo;
        DomainExample result = new DomainExample();
        BeanUtils.copyProperties(exampleDBO, result);
        return result;
    }
}
