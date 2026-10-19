package diario_emocional.ufrn.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RpdRequestDTO(
        @NotNull(message = "A intensidade da crise é obrigatória.")
        @Min(value = 1, message = "A intensidade da crise deve ser no mínimo 1.")
        @Max(value = 10, message = "A intensidade da crise deve ser no máximo 10.")
        Integer intensidadeCrise,
        List<@NotBlank(message = "A emoção não pode estar vazia.") String> emocoesPrincipais,
        String gatilhoSituacao,
        @NotBlank(message = "O pensamento automático é obrigatório.")
        String pensamentoAutomatico,
        List<@NotBlank(message = "A distorção cognitiva não pode estar vazia.") String> distorcoesCognitivas,
        String evidenciasContraArgumento,
        String pensamentoAlternativo,
        @Min(value = 1, message = "A intensidade reavaliada deve ser no mínimo 1.")
        @Max(value = 10, message = "A intensidade reavaliada deve ser no máximo 10.")
        Integer intensidadeReavaliada
) {
}
