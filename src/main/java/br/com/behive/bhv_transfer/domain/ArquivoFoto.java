package br.com.behive.bhv_transfer.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "arquivos_foto")
public class ArquivoFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private LoteUpload lote;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Column(name = "caminho_storage", nullable = false)
    private String caminhoStorage;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    public ArquivoFoto() {}

    public ArquivoFoto(LoteUpload lote, String nomeArquivo, String caminhoStorage, Long tamanhoBytes) {
        this.lote = lote;
        this.nomeArquivo = nomeArquivo;
        this.caminhoStorage = caminhoStorage;
        this.tamanhoBytes = tamanhoBytes;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LoteUpload getLote() { return lote; }
    public void setLote(LoteUpload lote) { this.lote = lote; }
    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public String getCaminhoStorage() { return caminhoStorage; }
    public void setCaminhoStorage(String caminhoStorage) { this.caminhoStorage = caminhoStorage; }
    public Long getTamanhoBytes() { return tamanhoBytes; }
    public void setTamanhoBytes(Long tamanhoBytes) { this.tamanhoBytes = tamanhoBytes; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}