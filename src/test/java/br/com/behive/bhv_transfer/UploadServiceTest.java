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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
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

    @BeforeEach
    void setUp() {
        siteId = UUID.randomUUID();
        tecnicoId = UUID.randomUUID();

        siteMock = new Site();
        tecnicoMock = new Tecnico("Leonardo Ferreira");

        loteMock = new LoteUpload(siteMock, tecnicoMock.getNome());
        loteMock.setId(UUID.randomUUID());


        ReflectionTestUtils.setField(uploadService, "baseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(uploadService, "pastaUpload", "uploads_teste");
    }

    @Test
    void deveSalvarVistoriaGerarLinkCorreto() throws IOException {
        when(siteRepository.findById(siteId)).thenReturn(Optional.of(siteMock));
        when(tecnicoRepository.findById(tecnicoId)).thenReturn(Optional.of(tecnicoMock));
        when(loteRepository.save(any(LoteUpload.class))).thenReturn(loteMock);


        MockMultipartFile arquivoMock = new MockMultipartFile(
                "fotos", "vistoria_mglgs02.rar", "application/x-rar-compressed", "conteudo fake".getBytes());


        Map<String, String> resultado = uploadService.salvarVistoria(siteId, tecnicoId, arquivoMock);


        assertNotNull(resultado);
        assertEquals("Leonardo Ferreira", resultado.get("tecnico"));
        assertTrue(resultado.get("link").contains("http://localhost:8080/vistorias/Leonardo_Ferreira/"));
        assertTrue(resultado.get("link").contains("vistoria_mglgs02.rar"));


        verify(fotoRepository, times(1)).save(any());
    }

    @Test
    void deveLancarErroSeSiteNaoExistir() {
        when(siteRepository.findById(siteId)).thenReturn(Optional.empty());
        MockMultipartFile arquivoMock = new MockMultipartFile("fotos", "teste.zip", "application/zip", new byte[0]);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            uploadService.salvarVistoria(siteId, tecnicoId, arquivoMock);
        });

        assertEquals("Site não encontrado", exception.getMessage());

        verify(fotoRepository, never()).save(any());
    }
}