package com.inswave.appplatform.dao;

import com.inswave.appplatform.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteDao extends JpaRepository<Site, Long> {

}
