package diario_emocional.ufrn.dto;

import diario_emocional.ufrn.enums.NivelDisposicao;
import diario_emocional.ufrn.enums.QualidadeSono;

import java.time.LocalDate;
import java.time.LocalTime;

public record RelatoHigieneSonoResponseDTO(
        Long id,
        LocalDate dataRegistro,
        LocalTime horaDormir,
        LocalTime horaAcordar,
        long duracaoSonoMinutos,
        QualidadeSono qualidadeSono,
        boolean usouCelular,
        boolean tevePesadelos,
        String comentarioSonhos,
        NivelDisposicao nivelDisposicao
) {
}