package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.SliderImagen;
import com.clubSocial.clubSocial.service.SliderImagenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sliders")
public class SliderImagenController {
    private final SliderImagenService service;

    public SliderImagenController(SliderImagenService service) {
        this.service = service;
    }

    @GetMapping
    public List<SliderImagen> all(){ return service.all(); }

    @GetMapping("/activos")
    public List<SliderImagen> activos(){ return service.activosOrdenados(); }

    @GetMapping("/{id}")
    public ResponseEntity<SliderImagen> byId(@PathVariable Long id){
        return service.byId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SliderImagen create(@RequestBody SliderImagen s){ return service.create(s); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SliderImagen> update(@PathVariable Long id, @RequestBody SliderImagen s){
        return service.update(id, s).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean ok = service.delete(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}