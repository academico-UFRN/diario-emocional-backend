package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.Sugestao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SugestaoRepository extends JpaRepository<Sugestao, Long> {
}
