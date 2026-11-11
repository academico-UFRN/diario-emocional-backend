package diario_emocional.ufrn.aplicacao.estudantes.repository;

import diario_emocional.ufrn.aplicacao.estudantes.dominio.RegistroAcademico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroAcademicoRepository extends JpaRepository<RegistroAcademico, Long> {
    List<RegistroAcademico> findAllByUsuario_IdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
            Long usuarioId,
            LocalDate inicio,
            LocalDate fim
    );

    List<RegistroAcademico> findAllByUsuario_IdOrderByDataReferenciaDesc(Long usuarioId);

    Optional<RegistroAcademico> findByIdAndUsuario_Id(Long id, Long usuarioId);
}
