package diario_emocional.ufrn.aplicacao.estudantes;

import diario_emocional.ufrn.aplicacao.estudantes.dominio.ContextoUniversitario;
import diario_emocional.ufrn.aplicacao.estudantes.dominio.RegistroAcademico;
import diario_emocional.ufrn.aplicacao.estudantes.repository.RegistroAcademicoRepository;
import diario_emocional.ufrn.framework.extension.ContextualizadorRegistros;
import diario_emocional.ufrn.framework.model.ContextoRegistros;
import diario_emocional.ufrn.framework.model.HistoricoEmocional;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ContextualizadorEstudantil implements ContextualizadorRegistros {
    private final RegistroAcademicoRepository registroAcademicoRepository;

    public ContextualizadorEstudantil(
            RegistroAcademicoRepository registroAcademicoRepository
    ) {
        this.registroAcademicoRepository = registroAcademicoRepository;
    }

    @Override
    public ContextoRegistros contextualizar(
            Long usuarioId,
            LocalDate dataReferencia,
            HistoricoEmocional historico
    ) {
        LocalDate inicio = dataReferencia.minusDays(6);
        LocalDate fim = dataReferencia.plusDays(30);

        List<ContextoUniversitario.ExperienciaUniversitaria> experiencias =
                registroAcademicoRepository
                        .findAllByUsuario_IdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
                                usuarioId, inicio, fim)
                        .stream()
                        .map(this::mapearExperiencia)
                        .toList();

        List<ContextoUniversitario.AtividadePlanejada> atividades =
                historico.atividades().stream()
                        .map(atividade -> new ContextoUniversitario.AtividadePlanejada(
                                atividade.titulo(),
                                atividade.subtitulo(),
                                atividade.horaInicio(),
                                atividade.horaFim(),
                                atividade.diasDaSemana()))
                        .toList();

        ContextoUniversitario dadosDominio =
                new ContextoUniversitario(experiencias, atividades);

        return new ContextoRegistros(
                "Esta aplicação destina-se a estudantes universitários. Considere "
                        + "provas, trabalhos, estudos e atividades acadêmicas estruturados "
                        + "com tipo, data e situação. Relacione registros emocionais e "
                        + "experiências acadêmicas pela data, sem inferir causalidade. "
                        + "Atividades planejadas não comprovam realização.",
                List.of("provas e avaliações", "trabalhos e atividades acadêmicas",
                        "estudos", "rotina universitária", "vida acadêmica"),
                dadosDominio);
    }

    private ContextoUniversitario.ExperienciaUniversitaria mapearExperiencia(
            RegistroAcademico registro
    ) {
        return new ContextoUniversitario.ExperienciaUniversitaria(
                "academico-" + registro.getId(),
                registro.getDataReferencia(),
                registro.getTipo(),
                registro.getTitulo(),
                registro.getDescricao(),
                registro.getSituacao());
    }
}
