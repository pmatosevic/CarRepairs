package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.AutoService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutoServiceRepository extends JpaRepository<AutoService, Long> {

    Optional<AutoService> getByOib(String oib);

}
