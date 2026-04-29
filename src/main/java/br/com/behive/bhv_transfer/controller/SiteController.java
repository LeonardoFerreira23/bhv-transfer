package br.com.behive.bhv_transfer.controller;

import br.com.behive.bhv_transfer.domain.Site;
import br.com.behive.bhv_transfer.repository.SiteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteRepository repository;

    public SiteController(SiteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Site> listarTodos() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Site> criar(@RequestBody Site site) {
        Site salvo = repository.save(site);
        return ResponseEntity.ok(salvo);
    }
}