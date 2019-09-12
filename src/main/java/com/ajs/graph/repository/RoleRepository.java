package com.ajs.graph.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajs.graph.model.Role;
import com.ajs.graph.model.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	@Query("select r from Role r where r.name like :roleName")
    Role findByRoleName(RoleName roleName);

}
