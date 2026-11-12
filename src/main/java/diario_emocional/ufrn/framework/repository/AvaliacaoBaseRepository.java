package diario_emocional.ufrn.framework.repository;

import diario_emocional.ufrn.framework.entity.Avaliacao;
import diario_emocional.ufrn.framework.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface AvaliacaoBaseRepository<T extends Avaliacao> extends JpaRepository<T, LocalDate> {
    List<T> findByUsuario(Usuario usuario);

    Optional<T> findByUsuarioAndDataRegistro(Usuario usuario, LocalDate dataRegistro);

    List<T> findAllByUsuario_IdAndDataRegistroBetween(Long usuarioId, LocalDate dataInicial, LocalDate dataFinal);

    List<T> findTop3ByUsuarioOrderByDataRegistroDesc(Usuario usuario);
}
