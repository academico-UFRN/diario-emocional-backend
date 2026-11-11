package diario_emocional.ufrn.aplicacao.estudantes.dto;

import diario_emocional.ufrn.aplicacao.estudantes.dominio.SituacaoExperienciaAcademica;
import diario_emocional.ufrn.aplicacao.estudantes.dominio.TipoExperienciaAcademica;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegistroAcademicoRequest(
        @NotNull TipoExperienciaAcademica tipo,
        @NotBlank @Size(max = 150) String titulo,
        @Size(max = 2000) String descricao,
        @NotNull LocalDate dataReferencia,
        @NotNull SituacaoExperienciaAcademica situacao
) {
}
