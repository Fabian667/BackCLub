package com.clubSocial.clubSocial.repository;

import com.clubSocial.clubSocial.model.SliderImagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SliderImagenRepository extends JpaRepository<SliderImagen, Long> {
    List<SliderImagen> findByActivoTrueOrderByOrdenAsc();
}