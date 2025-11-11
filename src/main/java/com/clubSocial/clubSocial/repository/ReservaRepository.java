package com.clubSocial.clubSocial.repository;

import com.clubSocial.clubSocial.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByFechaReservaGreaterThanEqual(LocalDate fecha);
    long countByFechaReservaGreaterThanEqual(LocalDate fecha);
}