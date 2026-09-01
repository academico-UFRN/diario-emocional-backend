package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RelatoDiaRepository extends JpaRepository<RelatoDia, LocalDate> {

    boolean existsByUsuarioIdAndDataRegistro(
            Long usuarioId,
            LocalDate dataRegistro
    );

    RelatoDia findRelatoDiasByUsuarioIdAndDataRegistro(Long usuarioId, LocalDate dataRegistro);

    List<RelatoDia> findAllByUsuarioId(Long usuarioId);

    List<RelatoDia> findAllByUsuarioIdAndDataRegistroBetween(Long usuarioId, LocalDate inicio, LocalDate fim);
}
