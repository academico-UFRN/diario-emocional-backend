package diario_emocional.ufrn.dto.IA;

import java.util.List;
import java.util.UUID;

public record ChatResponseDTO(
        UUID id,
        String titulo,
        String modelo,
        List<MensagemResponseDTO> mensagens
) {
}