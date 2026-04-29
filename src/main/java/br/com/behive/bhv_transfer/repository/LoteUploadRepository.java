package br.com.behive.bhv_transfer.repository;

import br.com.behive.bhv_transfer.domain.LoteUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface LoteUploadRepository extends JpaRepository<LoteUpload, UUID> {
}