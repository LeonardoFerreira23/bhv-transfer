package br.com.behive.bhv_transfer.controller;

import br.com.behive.bhv_transfer.domain.LoteUpload;
import br.com.behive.bhv_transfer.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
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
            @RequestParam("fotos") MultipartFile arquivo) { // Mudamos de List para MultipartFile único

        try {
            LoteUpload loteSalvo = uploadService.salvarVistoriaCompactada(siteId, nomeTecnico, arquivo);

            // Em vez de enviar apenas texto, enviamos um Map (que o Spring converte para JSON)
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Upload concluído com sucesso!");
            resposta.put("loteId", loteSalvo.getId().toString());

            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
}