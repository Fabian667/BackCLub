package com.clubSocial.clubSocial.service;

import com.clubSocial.clubSocial.model.SliderImagen;
import com.clubSocial.clubSocial.repository.SliderImagenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SliderImagenService {
    private final SliderImagenRepository repo;

    public SliderImagenService(SliderImagenRepository repo) {
        this.repo = repo;
    }

    public List<SliderImagen> all(){ return repo.findAll(); }

    public Optional<SliderImagen> byId(Long id){ return repo.findById(id); }

    public SliderImagen create(SliderImagen s){ return repo.save(s); }

    public Optional<SliderImagen> update(Long id, SliderImagen s){
        return repo.findById(id).map(existing -> {
            s.setId(existing.getId());
            return repo.save(s);
        });
    }

    public boolean delete(Long id){
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    public List<SliderImagen> activosOrdenados(){
        LocalDate hoy = LocalDate.now();
        return repo.findByActivoTrueOrderByOrdenAsc().stream()
                .filter(s -> (s.getFechaInicio() == null || !hoy.isBefore(s.getFechaInicio()))
                        && (s.getFechaFin() == null || !hoy.isAfter(s.getFechaFin())))
                .collect(Collectors.toList());
    }
}