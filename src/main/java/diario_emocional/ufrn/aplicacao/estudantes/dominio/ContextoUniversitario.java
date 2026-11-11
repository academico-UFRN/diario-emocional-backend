package diario_emocional.ufrn.aplicacao.estudantes.dominio;

import diario_emocional.ufrn.framework.model.ContextoAplicacao;

import java.time.LocalTime;
import java.util.List;

public record ContextoUniversitario(
        List<ExperienciaUniversitaria> experiencias,
        List<AtividadePlanejada> atividadesPlanejadas
) implements ContextoAplicacao {
    public ContextoUniversitario {
        experiencias = List.copyOf(experiencias);
        atividadesPlanejadas = List.copyOf(atividadesPlanejadas);
    }

    public record ExperienciaUniversitaria(
            String id,
            java.time.LocalDate data,
            TipoExperienciaAcademica tipo,
            String titulo,
            String descricao,
            SituacaoExperienciaAcademica situacao
    ) {
    }

    public record AtividadePlanejada(
            String titulo,
            String descricao,
            LocalTime horaInicio,
            LocalTime horaFim,
            List<String> diasDaSemana
    ) {
        public AtividadePlanejada {
            diasDaSemana = List.copyOf(diasDaSemana);
        }
    }

}
