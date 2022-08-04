package com.example.busmap.repository;

import com.example.busmap.domain.Info;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoRepository extends JpaRepository<Info, Long> {
    Info findByBusRouteId(Long routeId);
}
