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
    public LoteUpload salvarVistoria(UUID siteId, String nomeTecnico, List<MultipartFile> arquivos) throws IOException {
        // 1. Acha o Site no banco
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site não encontrado!"));

        // 2. Cria o Lote de Upload
        LoteUpload lote = new LoteUpload(site, nomeTecnico);
        lote = loteRepository.save(lote);

        // 3. Garante que a pasta existe no seu Windows
        Path caminhoPasta = Paths.get(pastaUpload);
        if (!Files.exists(caminhoPasta)) {
            Files.createDirectories(caminhoPasta);
        }

        // 4. Salva cada foto
        for (MultipartFile arquivo : arquivos) {
            if (arquivo.isEmpty()) continue;

            // Gera um nome único para não sobrescrever fotos com o mesmo nome
            String nomeUnico = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
            Path caminhoDestino = caminhoPasta.resolve(nomeUnico);

            // Copia o arquivo do Postman para a pasta do Windows
            arquivo.transferTo(caminhoDestino.toFile());

            // Salva o registro da foto no Banco de Dados
            ArquivoFoto foto = new ArquivoFoto(lote, arquivo.getOriginalFilename(), caminhoDestino.toString(), arquivo.getSize());
            fotoRepository.save(foto);
        }

        return lote;
    }
}