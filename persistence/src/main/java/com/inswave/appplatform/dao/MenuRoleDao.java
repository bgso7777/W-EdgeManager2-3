package com.inswave.appplatform.dao;

import com.inswave.appplatform.domain.MenuRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRoleDao extends JpaRepository<MenuRole, Long>, JpaSpecificationExecutor<MenuRole> {

    //@Query(value = "select m from MenuRole m where m.menuRoleId=:menuRoleId ", nativeQuery=false)
    //List<MenuRole> findByWhatever(Long menuRoleId);
    @Query(value = "select m from MenuRole m where m.menuRoleId=:menuRoleId ", nativeQuery=false)
    List<MenuRole> findByWhatever(Long menuRoleId);

//    @Query(value = "select m from MenuRole m where m.roleId=:roleId ", nativeQuery=false)
//    List<MenuRole> findByRoleId(Long roleId);

    List<MenuRole> findByRoleId(Long roleId);
}
