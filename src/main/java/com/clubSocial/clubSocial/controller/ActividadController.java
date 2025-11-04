package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.Actividad;
import com.clubSocial.clubSocial.repository.ActividadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {
    private final ActividadRepository repo;

    public ActividadController(ActividadRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Actividad> all(){ return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Actividad> byId(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Actividad create(@RequestBody Actividad a){ return repo.save(a); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Actividad> update(@PathVariable Long id, @RequestBody Actividad a){
        return repo.findById(id).map(existing -> {
            a.setId(existing.getId());
            return ResponseEntity.ok(repo.save(a));
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