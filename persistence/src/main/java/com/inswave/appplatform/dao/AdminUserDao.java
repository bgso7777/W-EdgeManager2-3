package com.inswave.appplatform.dao;

import com.inswave.appplatform.domain.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserDao extends JpaRepository<AdminUser, Long>, JpaSpecificationExecutor<AdminUser> {

    Optional<AdminUser> findByLoginId(String loginId);

//    AdminUser findByAuthToken(String authToken);  // 미사용 (토큰 DB저장안함)
/*
    @Modifying
    @Transactional
    //@Query(value="update AdminUser a set a.password=:#{#columnValue} where a.adminUserId=:#{#id}")
    //@Query(value="update admin_user set :#{#columnName}=:#{#columnValue} where ADMIN_USER_ID=:#{#id}", nativeQuery=true)
    @Query(value="update admin_user set :columnName=:#{#columnValue} where ADMIN_USER_ID=:#{#id}", nativeQuery=true)
    int updateColumn(@Param("columnName") Object columnName, @Param("columnValue") String columnValue, @Param("id") Long id);
*/

}
