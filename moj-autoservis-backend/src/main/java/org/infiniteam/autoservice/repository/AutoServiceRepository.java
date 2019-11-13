package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.AutoService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoServiceRepository extends JpaRepository<AutoService, Long> {
}
