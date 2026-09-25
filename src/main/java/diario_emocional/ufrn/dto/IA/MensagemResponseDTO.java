package diario_emocional.ufrn.dto.IA;

import diario_emocional.ufrn.enums.MensagemRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record MensagemResponseDTO(
        UUID id,
        String conteudo,
        MensagemRole papel,
        LocalDateTime criadoEm
) {}