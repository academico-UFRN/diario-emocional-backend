package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.dto.IA.ChatResumoDTO;
import diario_emocional.ufrn.entity.Chat;
import diario_emocional.ufrn.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Optional<Chat> findByIdAndUsuario(UUID uuid, Usuario usuario);

    @Query("SELECT new diario_emocional.ufrn.dto.IA.ChatResumoDTO(c.id, c.titulo) FROM Chat c WHERE c.usuario = :usuario")
    List<ChatResumoDTO> findResumoByUsuario(@Param("usuario") Usuario usuario);
}
