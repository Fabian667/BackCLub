package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.InformacionClub;
import com.clubSocial.clubSocial.service.InformacionClubService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/informacion")
public class InformacionClubController {
    private final InformacionClubService service;

    public InformacionClubController(InformacionClubService service) {
        this.service = service;
    }

    @GetMapping
    public List<InformacionClub> all(){ return service.all(); }

    @GetMapping("/{id}")
    public ResponseEntity<InformacionClub> byId(@PathVariable Long id){
        return service.byId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public InformacionClub create(@RequestBody InformacionClub info){ return service.create(info); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InformacionClub> update(@PathVariable Long id, @RequestBody InformacionClub info){
        return service.update(id, info).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean ok = service.delete(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}