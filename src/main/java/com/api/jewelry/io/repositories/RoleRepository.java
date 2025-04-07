package com.api.jewelry.io.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.api.jewelry.io.entity.RoleEntity;
import com.api.jewelry.shared.RoleName;

import java.util.Optional;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, Long> {
//    Optional<RoleEntity> findByName(RoleName roleUser);

	RoleEntity findByRoleName(String userRole);

}
