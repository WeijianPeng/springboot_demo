package com.china.demo.example.infrastructure.persistence;

import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractRepositoryImpl {


    protected List convertDBOListToDomainList(List dboList) {
        if (dboList instanceof Page) {
            Page dboPageList = (Page) dboList;
            List results = toDomainList(dboPageList.getResult());
            Page domainPageList = new Page<>();
            BeanUtils.copyProperties(dboPageList, domainPageList);
            domainPageList.clear();
            domainPageList.addAll(results);
            return domainPageList;
        } else {
            return toDomainList(dboList);
        }
    }

    private List toDomainList(List result) {
        return (List) result.stream()
                .map(dbo -> toDomainModel(dbo))
                .collect(Collectors.toList());
    }

    protected abstract Object toDomainModel(Object dbo);

    protected abstract Object toDBO(Object domainModel);

}
