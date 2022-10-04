package com.inswave.appplatform.dao;

import com.inswave.appplatform.domain.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseDao extends JpaRepository<License, Long> {

}
