package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RelatoDiaRepository extends JpaRepository<RelatoDia, LocalDate> {

    boolean existsByUsuarioIdAndDataRegistro(
            Long usuarioId,
            LocalDate dataRegistro
    );
}
