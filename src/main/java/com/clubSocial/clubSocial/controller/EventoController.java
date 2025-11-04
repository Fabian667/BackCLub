package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.Evento;
import com.clubSocial.clubSocial.repository.EventoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {
    private final EventoRepository repo;

    public EventoController(EventoRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Evento> all(){ return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> byId(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Evento create(@RequestBody Evento e){ return repo.save(e); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Evento> update(@PathVariable Long id, @RequestBody Evento e){
        return repo.findById(id).map(existing -> {
            e.setId(existing.getId());
            return ResponseEntity.ok(repo.save(e));
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