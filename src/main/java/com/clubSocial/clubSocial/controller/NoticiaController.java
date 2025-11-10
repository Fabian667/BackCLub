package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.Noticia;
import com.clubSocial.clubSocial.repository.NoticiaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/noticias")
public class NoticiaController {
    private final NoticiaRepository repo;

    public NoticiaController(NoticiaRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Noticia> publicadas(Authentication auth){
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        return isAdmin ? repo.findAll() : repo.findByEstado(Noticia.Estado.PUBLICADA);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Noticia> todas(){ return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Noticia> byId(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Noticia create(@RequestBody Noticia n){ return repo.save(n); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Noticia> update(@PathVariable Long id, @RequestBody Noticia n){
        return repo.findById(id).map(existing -> {
            n.setId(existing.getId());
            return ResponseEntity.ok(repo.save(n));
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