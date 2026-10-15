package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static org.hibernate.boot.model.NamedEntityGraphDefinition.Source.JPA;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    List<Solicitacao> findAllByUsuario_Id(Long id);

    boolean existsByUsuario_IdAndId(Long usuarioId, Long id);

    Solicitacao findByUsuario_IdAndId(Long usuarioId, Long id);
}
