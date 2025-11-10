package com.clubSocial.clubSocial.repository;

import com.clubSocial.clubSocial.model.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {
    List<Noticia> findByEstado(Noticia.Estado estado);
}