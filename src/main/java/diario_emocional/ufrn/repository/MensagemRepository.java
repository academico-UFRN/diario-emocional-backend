package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {
    List<Mensagem> findTop10ByChatIdOrderByCriadoEmDesc(UUID chatId);
}
