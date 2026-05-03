package br.com.behive.bhv_transfer.controller;

import br.com.behive.bhv_transfer.domain.Tecnico;
import br.com.behive.bhv_transfer.repository.TecnicoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final TecnicoRepository tecnicoRepository;

    public TecnicoController(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    @PostMapping
    public ResponseEntity<Tecnico> criarTecnico(@RequestBody Tecnico tecnico) {
        Tecnico salvo = tecnicoRepository.save(tecnico);
        return ResponseEntity.ok(salvo);
    }
}