package diario_emocional.ufrn.framework.service;

import diario_emocional.ufrn.entity.*;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.framework.model.HistoricoEmocional;
import diario_emocional.ufrn.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistoricoEmocionalService {
    private final UsuarioRepository usuarioRepository;
    private final RelatoDiaRepository relatoDiaRepository;
    private final AvalicaoSentimentoRepository avaliacaoRepository;
    private final RpdRepository rpdRepository;
    private final RelatoHigieneSonoRepository sonoRepository;
    private final AtividadeObrigatoriaRepository atividadeRepository;

    public HistoricoEmocionalService(
            UsuarioRepository usuarioRepository,
            RelatoDiaRepository relatoDiaRepository,
            AvalicaoSentimentoRepository avaliacaoRepository,
            RpdRepository rpdRepository,
            RelatoHigieneSonoRepository sonoRepository,
            AtividadeObrigatoriaRepository atividadeRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.relatoDiaRepository = relatoDiaRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.rpdRepository = rpdRepository;
        this.sonoRepository = sonoRepository;
        this.atividadeRepository = atividadeRepository;
    }

    @Transactional(readOnly = true)
    public HistoricoEmocional carregar(Long usuarioId, LocalDate data) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o ID: " + usuarioId));

        LocalDate inicio = data.minusDays(6);

        List<HistoricoEmocional.Relato> relatos =
                relatoDiaRepository.findAllByUsuarioIdAndDataRegistroBetween(usuarioId, inicio, data)
                        .stream()
                        .map(relato -> new HistoricoEmocional.Relato(
                                relato.getDataRegistro(),
                                relato.getTitulo(),
                                relato.getConteudoHtml()))
                        .toList();

        List<HistoricoEmocional.Avaliacao> avaliacoes =
                avaliacaoRepository.findAllByUsuario_IdAndDataRegistroBetween(
                                usuarioId, data.minusDays(7), data)
                        .stream()
                        .map(this::mapearAvaliacao)
                        .toList();

        List<HistoricoEmocional.Pensamento> pensamentos =
                rpdRepository.findAllByUsuario_IdOrderByDataHoraRegistroDesc(usuarioId)
                        .stream()
                        .filter(registro -> {
                            LocalDate registroData = registro.getDataHoraRegistro().toLocalDate();
                            return !registroData.isBefore(inicio) && !registroData.isAfter(data);
                        })
                        .map(registro -> new HistoricoEmocional.Pensamento(
                                registro.getDataHoraRegistro().toLocalDate(),
                                registro.getIntensidadeCrise(),
                                registro.getGatilhoSituacao(),
                                registro.getPensamentoAutomatico(),
                                registro.getEmocoesPrincipais()))
                        .toList();

        List<HistoricoEmocional.Sono> sono =
                sonoRepository.findAllByUsuario_IdAndDataRegistroBetween(usuarioId, inicio, data)
                        .stream()
                        .map(registro -> new HistoricoEmocional.Sono(
                                registro.getDataRegistro(),
                                registro.getHoraDormir(),
                                registro.getHoraAcordar(),
                                registro.getDuracaoSonoMinutos(),
                                registro.getQualidadeSono().name(),
                                registro.getNivelDisposicao().name(),
                                registro.getComentarioSonhos()))
                        .toList();

        List<HistoricoEmocional.Atividade> atividades =
                atividadeRepository.findAllByUsuario_IdAndAtivoTrue(usuarioId)
                        .stream()
                        .map(atividade -> new HistoricoEmocional.Atividade(
                                atividade.getTitulo(),
                                atividade.getSubtitulo(),
                                atividade.getHoraInicio(),
                                atividade.getHoraFim(),
                                atividade.getDiasDaSemana().stream()
                                        .map(Enum::name)
                                        .toList()))
                        .toList();

        return new HistoricoEmocional(relatos, avaliacoes, pensamentos, sono, atividades);
    }

    private HistoricoEmocional.Avaliacao mapearAvaliacao(AvaliacaoSentimento avaliacao) {
        List<HistoricoEmocional.SentimentoRegistrado> sentimentos =
                avaliacao.getSentimentos().stream()
                        .map(sentimento -> new HistoricoEmocional.SentimentoRegistrado(
                                sentimento.getSentimento(),
                                sentimento.getIntensidade()))
                        .toList();

        return new HistoricoEmocional.Avaliacao(
                avaliacao.getDataRegistro(),
                avaliacao.getAvaliacaoDia(),
                avaliacao.getTextoLivre(),
                avaliacao.getGatilhos(),
                sentimentos);
    }
}
