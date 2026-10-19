package diario_emocional.ufrn.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RpdResponseDTO(
        Long id,
        LocalDateTime dataHoraRegistro,
        Integer intensidadeCrise,
        List<String> emocoesPrincipais,
        String gatilhoSituacao,
        String pensamentoAutomatico,
        List<String> distorcoesCognitivas,
        String evidenciasContraArgumento,
        String pensamentoAlternativo,
        Integer intensidadeReavaliada
) {
}
