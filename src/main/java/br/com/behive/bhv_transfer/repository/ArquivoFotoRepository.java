package br.com.behive.bhv_transfer.repository;

import br.com.behive.bhv_transfer.domain.ArquivoFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ArquivoFotoRepository extends JpaRepository<ArquivoFoto, UUID> {
}