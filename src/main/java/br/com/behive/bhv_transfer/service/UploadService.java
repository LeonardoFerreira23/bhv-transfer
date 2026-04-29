package br.com.behive.bhv_transfer.service;

import br.com.behive.bhv_transfer.domain.ArquivoFoto;
import br.com.behive.bhv_transfer.domain.LoteUpload;
import br.com.behive.bhv_transfer.domain.Site;
import br.com.behive.bhv_transfer.repository.ArquivoFotoRepository;
import br.com.behive.bhv_transfer.repository.LoteUploadRepository;
import br.com.behive.bhv_transfer.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UploadService {

    private final SiteRepository siteRepository;
    private final LoteUploadRepository loteRepository;
    private final ArquivoFotoRepository fotoRepository;

    @Value("${app.pasta.upload}")
    private String pastaUpload;

    public UploadService(SiteRepository siteRepository, LoteUploadRepository loteRepository, ArquivoFotoRepository fotoRepository) {
        this.siteRepository = siteRepository;
        this.loteRepository = loteRepository;
        this.fotoRepository = fotoRepository;
    }

    @Transactional
    public LoteUpload salvarVistoriaCompactada(UUID siteId, String nomeTecnico, MultipartFile arquivoZip) throws IOException {
        // 1. Valida o Site
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site não encontrado!"));

        // 2. Cria o registro do Lote
        LoteUpload lote = new LoteUpload(site, nomeTecnico);
        lote = loteRepository.save(lote);

        // 3. Define e cria a pasta física no servidor da Behive
        Path caminhoPasta = Paths.get(pastaUpload);
        if (!Files.exists(caminhoPasta)) {
            Files.createDirectories(caminhoPasta);
        }

        // 4. Salva o arquivo ZIP/RAR com nome único para evitar sobreposição
        String nomeUnico = UUID.randomUUID().toString() + "_" + arquivoZip.getOriginalFilename();
        Path caminhoDestino = caminhoPasta.resolve(nomeUnico);
        arquivoZip.transferTo(caminhoDestino.toFile());

        // 5. Registra no banco que este lote possui este arquivo específico
        ArquivoFoto registro = new ArquivoFoto(lote, arquivoZip.getOriginalFilename(), caminhoDestino.toString(), arquivoZip.getSize());
        fotoRepository.save(registro);

        return lote;
    }
}