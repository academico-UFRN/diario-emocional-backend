package diario_emocional.ufrn.dto;

import diario_emocional.ufrn.enums.NivelDisposicao;
import diario_emocional.ufrn.enums.QualidadeSono;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record RelatoHigieneSonoRequestDTO(
        @NotNull LocalDate dataRegistro,
        @NotNull LocalTime horaDormir,
        @NotNull LocalTime horaAcordar,
        @NotNull QualidadeSono qualidadeSono,
        @NotNull Boolean usouCelular,
        @NotNull Boolean tevePesadelos,
        @Size(max = 1000) String comentarioSonhos,
        @NotNull NivelDisposicao nivelDisposicao
) {
}