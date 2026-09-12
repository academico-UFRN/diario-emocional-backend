package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.enums.DiaSemana;
import diario_emocional.ufrn.entity.AtividadeObrigatoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtividadeObrigatoriaRepository extends JpaRepository<AtividadeObrigatoria, Long> {

    List<AtividadeObrigatoria> findByUsuarioIdAndAtivoTrue(Long usuarioId);

    Optional<AtividadeObrigatoria> findByIdAndUsuarioIdAndAtivoTrue(Long id, Long usuarioId);

    @Query("SELECT a FROM AtividadeObrigatoria a JOIN a.diasDaSemana d WHERE a.usuarioId = :usuarioId AND a.ativo = true AND d = :dia")
    List<AtividadeObrigatoria> findByUsuarioIdAndDiaSemana(
        @Param("usuarioId") Long usuarioId,
        @Param("dia") DiaSemana dia
    );
}