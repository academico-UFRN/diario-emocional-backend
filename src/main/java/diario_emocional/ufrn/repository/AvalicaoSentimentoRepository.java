package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.entity.sentimento.AvaliacaoSentimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvalicaoSentimentoRepository extends JpaRepository<AvaliacaoSentimento, LocalDate> {
    List<AvaliacaoSentimento> findByUsuario(Usuario usuario);

    Optional<AvaliacaoSentimento> findByUsuarioAndDataRegistro(Usuario usuario, LocalDate dataRegistro);
}
