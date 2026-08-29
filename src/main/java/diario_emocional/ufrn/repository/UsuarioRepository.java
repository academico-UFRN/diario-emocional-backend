package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
}
