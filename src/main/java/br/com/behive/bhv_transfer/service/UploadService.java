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
import java.nio.file.StandardOpenOption;
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
    public UploadService(SiteRepository siteRepository, TecnicoRepository tecnicoRepository, LoteUploadRepository loteRepository, ArquivoFotoRepository fotoRepository) {
            this.siteRepository = siteRepository;
            this.tecnicoRepository = tecnicoRepository;
            this.loteRepository = loteRepository;
            this.fotoRepository = fotoRepository;
        }

        @Transactional
        public Map<String, String> processarUpload(UUID siteId, UUID tecnicoId, MultipartFile chunk, String dzuuid, Integer chunkIndex, Integer totalChunks) throws IOException {

            if (dzuuid == null) dzuuid = UUID.randomUUID().toString();

            Path pastaTemp = Paths.get(pastaUpload, "temp", dzuuid);
            Files.createDirectories(pastaTemp);

            Path arquivoChunk = pastaTemp.resolve("chunk_" + chunkIndex);
            chunk.transferTo(arquivoChunk.toFile());

            if (chunkIndex < totalChunks - 1) {
                return Map.of("mensagem", "Fatia " + (chunkIndex + 1) + " de " + totalChunks + " recebida.");
            }

            Site site = siteRepository.findById(siteId).orElseThrow(() -> new RuntimeException("Site não encontrado"));
            Tecnico tecnico = tecnicoRepository.findById(tecnicoId).orElseThrow(() -> new RuntimeException("Técnico não encontrado"));

            LoteUpload lote = new LoteUpload(site, tecnico.getNome());
            lote = loteRepository.save(lote);

            String nomePasta = tecnico.getNome().replaceAll(" ", "_");
            Path caminhoFinal = Paths.get(pastaUpload, nomePasta, lote.getId().toString());
            Files.createDirectories(caminhoFinal);

            String nomeOriginal = chunk.getOriginalFilename();
            Path arquivoFinal = caminhoFinal.resolve(nomeOriginal);

            try (var outputStream = Files.newOutputStream(arquivoFinal, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                for (int i = 0; i < totalChunks; i++) {
                    Path pedaco = pastaTemp.resolve("chunk_" + i);
                    Files.copy(pedaco, outputStream);
                    Files.delete(pedaco);
            }

            Files.deleteIfExists(pastaTemp);

            ArquivoFoto registro = new ArquivoFoto(lote, nomeOriginal, arquivoFinal.toString(), Files.size(arquivoFinal));
            fotoRepository.save(registro);

            String link = baseUrl + "/vistorias/" + nomePasta + "/" + lote.getId() + "/" + nomeOriginal;

            return Map.of(
                    "mensagem", "Upload concluído!",
                    "link", link,
                    "tecnico", tecnico.getNome()
            );
        }
    }
}