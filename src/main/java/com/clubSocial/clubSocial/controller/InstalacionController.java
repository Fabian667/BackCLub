package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.Instalacion;
import com.clubSocial.clubSocial.repository.InstalacionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instalaciones")
public class InstalacionController {
    private final InstalacionRepository repo;

    public InstalacionController(InstalacionRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Instalacion> all(){ return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Instalacion> byId(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Instalacion create(@RequestBody Instalacion i){ return repo.save(i); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Instalacion> update(@PathVariable Long id, @RequestBody Instalacion i){
        return repo.findById(id).map(existing -> {
            i.setId(existing.getId());
            return ResponseEntity.ok(repo.save(i));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}