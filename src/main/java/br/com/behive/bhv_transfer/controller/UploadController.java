package br.com.behive.bhv_transfer.controller;

import br.com.behive.bhv_transfer.repository.SiteRepository;
import br.com.behive.bhv_transfer.repository.TecnicoRepository;
import br.com.behive.bhv_transfer.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private final UploadService uploadService;
    private final SiteRepository siteRepository;
    private final TecnicoRepository tecnicoRepository;

    public UploadController(UploadService uploadService,
                            SiteRepository siteRepository,
                            TecnicoRepository tecnicoRepository) {
        this.uploadService = uploadService;
        this.siteRepository = siteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @PostMapping
    public ResponseEntity<?> receberUpload(
            @RequestParam("siteId") UUID siteId,
            @RequestParam("tecnicoId") UUID tecnicoId,
            @RequestParam(value = "dzuuid", required = false) String dzuuid,
            @RequestParam(value = "dzchunkindex", defaultValue = "0") Integer chunkIndex,
            @RequestParam(value = "dztotalchunkcount", defaultValue = "1") Integer totalChunks,
            @RequestParam("fotos") MultipartFile arquivo) {

        try {
            // Repassamos as informações de fatiamento para o Service
            Map<String, String> dadosUpload = uploadService.processarUpload(
                    siteId, tecnicoId, arquivo, dzuuid, chunkIndex, totalChunks);

            return ResponseEntity.ok(dadosUpload);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/dados-iniciais")
    public ResponseEntity<?> buscarDadosIniciais() {
        return ResponseEntity.ok(Map.of(
                "tecnicos", tecnicoRepository.findAll(),
                "sites", siteRepository.findAll()
        ));
    }
}