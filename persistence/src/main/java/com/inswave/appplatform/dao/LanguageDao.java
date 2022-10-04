package com.inswave.appplatform.dao;

import com.inswave.appplatform.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageDao extends JpaRepository<Language, Long> {

}
