package com.example.busmap.repository;

import com.example.busmap.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {
    Bus findByBusNum(String BusNum);
}
