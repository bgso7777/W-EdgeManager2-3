package com.inswave.appplatform.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface StandardDao<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
