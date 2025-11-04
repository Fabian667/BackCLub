package com.clubSocial.clubSocial.service;

import com.clubSocial.clubSocial.model.InformacionClub;
import com.clubSocial.clubSocial.repository.InformacionClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InformacionClubService {
    private final InformacionClubRepository repo;

    public InformacionClubService(InformacionClubRepository repo) {
        this.repo = repo;
    }

    public List<InformacionClub> all(){ return repo.findAll(); }

    public Optional<InformacionClub> byId(Long id){ return repo.findById(id); }

    public InformacionClub create(InformacionClub info){ return repo.save(info); }

    public Optional<InformacionClub> update(Long id, InformacionClub info){
        return repo.findById(id).map(existing -> {
            info.setId(existing.getId());
            return repo.save(info);
        });
    }

    public boolean delete(Long id){
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}