package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.Evento;
import com.clubSocial.clubSocial.model.Instalacion;
import com.clubSocial.clubSocial.repository.EventoRepository;
import com.clubSocial.clubSocial.repository.InstalacionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {
    private final EventoRepository repo;
    private final InstalacionRepository instalacionRepo;

    public EventoController(EventoRepository repo, InstalacionRepository instalacionRepo) {
        this.repo = repo;
        this.instalacionRepo = instalacionRepo;
    }

    @GetMapping
    public List<Evento> all(){ return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> byId(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public record CreateEventoRequest(
            String titulo,
            String descripcion,
            String lugar,
            LocalDateTime fechaEvento,
            LocalDateTime fechaFin,
            Long instalacionId,
            Integer cupoMaximo,
            BigDecimal precio,
            Evento.TipoEvento tipoEvento,
            String imagen,
            Evento.Estado estado
    ) {}

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Evento> create(@RequestBody CreateEventoRequest req){
        Evento e = new Evento();
        e.setTitulo(req.titulo());
        e.setDescripcion(req.descripcion());
        e.setLugar(req.lugar());
        e.setFechaEvento(req.fechaEvento());
        e.setFechaFin(req.fechaFin());
        if (req.instalacionId() != null) {
            Instalacion inst = instalacionRepo.findById(req.instalacionId()).orElseThrow();
            e.setInstalacion(inst);
        }
        e.setCupoMaximo(req.cupoMaximo());
        e.setPrecio(req.precio());
        e.setTipoEvento(req.tipoEvento());
        e.setImagen(req.imagen());
        e.setEstado(req.estado());
        return ResponseEntity.ok(repo.save(e));
    }

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