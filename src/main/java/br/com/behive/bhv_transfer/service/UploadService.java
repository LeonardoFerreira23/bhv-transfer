package br.com.behive.bhv_transfer.service;

import br.com.behive.bhv_transfer.domain.ArquivoFoto;
import br.com.behive.bhv_transfer.domain.LoteUpload;
import br.com.behive.bhv_transfer.domain.Site;
import br.com.behive.bhv_transfer.domain.Tecnico;
import br.com.behive.bhv_transfer.repository.ArquivoFotoRepository;
import br.com.behive.bhv_transfer.repository.LoteUploadRepository;
import br.com.behive.bhv_transfer.repository.SiteRepository;
import br.com.behive.bhv_transfer.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class UploadService {

    @Value("${app.base.url:http://localhost:8080}")
    private String baseUrl;

    @Value("${app.pasta.upload}")
    private String pastaUpload;

    private final SiteRepository siteRepository;
    private final TecnicoRepository tecnicoRepository;
    private final LoteUploadRepository loteRepository;
    private final ArquivoFotoRepository fotoRepository;

    public UploadService(SiteRepository siteRepository,
                         TecnicoRepository tecnicoRepository,
                         LoteUploadRepository loteRepository,
                         ArquivoFotoRepository fotoRepository) {
        this.siteRepository = siteRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.loteRepository = loteRepository;
        this.fotoRepository = fotoRepository;
    }

    @Transactional
    public Map<String, String> salvarVistoria(UUID siteId, UUID tecnicoId, MultipartFile arquivo) throws IOException {

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site não encontrado"));

        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));

        LoteUpload lote = new LoteUpload(site, tecnico.getNome());
        lote = loteRepository.save(lote);

        String nomePasta = tecnico.getNome().replaceAll(" ", "_");
        Path caminho = Paths.get(pastaUpload, nomePasta, lote.getId().toString());
        Files.createDirectories(caminho);

        String nomeFinal = arquivo.getOriginalFilename();
        Path caminhoDestino = caminho.resolve(nomeFinal);
        arquivo.transferTo(caminhoDestino.toFile());

        ArquivoFoto registro = new ArquivoFoto(lote, nomeFinal, caminhoDestino.toString(), arquivo.getSize());
        fotoRepository.save(registro);

        String link = baseUrl + "/vistorias/" + nomePasta + "/" + lote.getId() + "/" + nomeFinal;

        return Map.of(
                "mensagem", "Upload realizado com sucesso!",
                "link", link,
                "tecnico", tecnico.getNome()
        );
    }
}