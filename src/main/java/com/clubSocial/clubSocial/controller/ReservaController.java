package com.clubSocial.clubSocial.controller;

import com.clubSocial.clubSocial.model.Instalacion;
import com.clubSocial.clubSocial.model.Reserva;
import com.clubSocial.clubSocial.model.Usuario;
import com.clubSocial.clubSocial.repository.InstalacionRepository;
import com.clubSocial.clubSocial.repository.ReservaRepository;
import com.clubSocial.clubSocial.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    private final ReservaRepository repo;
    private final InstalacionRepository instalacionRepo;
    private final UsuarioRepository usuarioRepo;

    public ReservaController(ReservaRepository repo, InstalacionRepository instalacionRepo, UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.instalacionRepo = instalacionRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public List<Reserva> all(){ return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> byId(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public long count(){ return repo.count(); }

    public record CreateReservaRequest(Long instalacionId, LocalDate fechaReserva, LocalTime horaInicio, LocalTime horaFin, Integer cantidadPersonas, String motivo, String observaciones) {}

    @PostMapping
    public ResponseEntity<Reserva> create(@RequestBody CreateReservaRequest req, Authentication auth){
        Instalacion inst = instalacionRepo.findById(req.instalacionId()).orElseThrow();
        String email = auth.getName();
        Usuario user = usuarioRepo.findByEmail(email).orElseThrow();
        Reserva r = new Reserva();
        r.setInstalacion(inst);
        r.setUsuario(user);
        r.setFechaReserva(req.fechaReserva());
        r.setHoraInicio(req.horaInicio());
        r.setHoraFin(req.horaFin());
        r.setEstado(Reserva.Estado.PENDIENTE);
        r.setCantidadPersonas(req.cantidadPersonas());
        r.setMotivo(req.motivo());
        r.setObservaciones(req.observaciones());
        long minutes = Duration.between(req.horaInicio(), req.horaFin()).toMinutes();
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal precio = inst.getPrecioHora() != null ? inst.getPrecioHora().multiply(hours) : BigDecimal.ZERO;
        r.setPrecioTotal(precio);
        r.setCostoTotal(precio);
        return ResponseEntity.ok(repo.save(r));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id){
        return repo.findById(id).map(reserva -> {
            reserva.setEstado(Reserva.Estado.CANCELADA);
            reserva.setFechaCancelacion(java.time.LocalDateTime.now());
            return ResponseEntity.ok(repo.save(reserva));
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