package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.AutoService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoServiceRepository extends JpaRepository<AutoService, Long> {

    List<AutoService> findAllByActive(boolean active);

    Optional<AutoService> getByOib(String oib);

    boolean existsByOib(String oib);

}
