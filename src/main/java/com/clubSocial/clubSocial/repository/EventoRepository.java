package com.clubSocial.clubSocial.repository;

import com.clubSocial.clubSocial.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}