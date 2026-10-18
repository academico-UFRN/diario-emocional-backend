package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.RelatoHigieneSono;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RelatoHigieneSonoRepository extends JpaRepository<RelatoHigieneSono, Long> {
    boolean existsByUsuario_IdAndDataRegistro(Long usuarioId, java.time.LocalDate dataRegistro);

    List<RelatoHigieneSono> findAllByUsuario_IdOrderByDataRegistroDesc(Long usuarioId);

    Optional<RelatoHigieneSono> findByIdAndUsuario_Id(Long id, Long usuarioId);

    List<RelatoHigieneSono> findAllByUsuario_IdAndDataRegistroBetween(Long usuarioId, LocalDate dataRegistroAntiga, LocalDate dataAtual);
}