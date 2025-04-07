package com.api.jewelry.io.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.jewelry.io.entity.KaratEntity;

@Repository
public interface KaratRepository extends JpaRepository<KaratEntity, Long> {
// Custom query methods can be added here if needed
}
