package br.com.behive.bhv_transfer.controller;

import br.com.behive.bhv_transfer.domain.LoteUpload;
import br.com.behive.bhv_transfer.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public ResponseEntity<?> receberUpload(
            @RequestParam("siteId") UUID siteId,
            @RequestParam("nomeTecnico") String nomeTecnico,
            @RequestParam("fotos") List<MultipartFile> fotos) {

        try {
            LoteUpload loteSalvo = uploadService.salvarVistoria(siteId, nomeTecnico, fotos);
            return ResponseEntity.ok("Upload concluído com sucesso! ID do Lote: " + loteSalvo.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar upload: " + e.getMessage());
        }
    }
}