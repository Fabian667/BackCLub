package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.Pago;
import com.clubSocial.clubSocial.model.Usuario;
import com.clubSocial.clubSocial.repository.PagoRepository;
import com.clubSocial.clubSocial.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    private final PagoRepository repo;
    private final UsuarioRepository usuarioRepo;

    public PagoController(PagoRepository repo, UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Pago> all(){ return repo.findAll(); }

    @PostMapping
    public ResponseEntity<Pago> create(@RequestBody Pago p, Authentication auth){
        String email = auth.getName();
        Usuario u = usuarioRepo.findByEmail(email).orElseThrow();
        p.setUsuario(u);
        return ResponseEntity.ok(repo.save(p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> byId(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}