package br.com.behive.bhv_transfer;

import br.com.behive.bhv_transfer.domain.LoteUpload;
import br.com.behive.bhv_transfer.domain.Site;
import br.com.behive.bhv_transfer.domain.Tecnico;
import br.com.behive.bhv_transfer.repository.ArquivoFotoRepository;
import br.com.behive.bhv_transfer.repository.LoteUploadRepository;
import br.com.behive.bhv_transfer.repository.SiteRepository;
import br.com.behive.bhv_transfer.repository.TecnicoRepository;
import br.com.behive.bhv_transfer.service.UploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadServiceTest {

    @Mock private SiteRepository siteRepository;
    @Mock private TecnicoRepository tecnicoRepository;
    @Mock private LoteUploadRepository loteRepository;
    @Mock private ArquivoFotoRepository fotoRepository;

    @InjectMocks
    private UploadService uploadService;

    private UUID siteId;
    private UUID tecnicoId;
    private Site siteMock;
    private Tecnico tecnicoMock;
    private LoteUpload loteMock;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        siteId = UUID.randomUUID();
        tecnicoId = UUID.randomUUID();
        siteMock = new Site();
        tecnicoMock = new Tecnico("Leonardo Ferreira");

        loteMock = new LoteUpload(siteMock, tecnicoMock.getNome());
        loteMock.setId(UUID.randomUUID());

        ReflectionTestUtils.setField(uploadService, "baseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(uploadService, "pastaUpload", tempDir.toString());
    }

    @Test
    void deveProcessarUploadFatiadoEGerarLink() throws IOException {
        when(siteRepository.findById(siteId)).thenReturn(Optional.of(siteMock));
        when(tecnicoRepository.findById(tecnicoId)).thenReturn(Optional.of(tecnicoMock));
        when(loteRepository.save(any(LoteUpload.class))).thenReturn(loteMock);

        MockMultipartFile arquivoMock = new MockMultipartFile(
                "fotos", "vistoria_mglgs02.rar", "application/x-rar-compressed", "conteudo fake".getBytes());

        Map<String, String> resultado = uploadService.processarUpload(
                siteId, tecnicoId, arquivoMock, "dzuuid-fake", 0, 1);

        assertNotNull(resultado);
        assertEquals("Leonardo Ferreira", resultado.get("tecnico"));
        assertTrue(resultado.get("link").contains("vistoria_mglgs02.rar"));

        verify(fotoRepository, times(1)).save(any());
    }
}