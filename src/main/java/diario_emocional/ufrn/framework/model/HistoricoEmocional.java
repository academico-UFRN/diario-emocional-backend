package diario_emocional.ufrn.framework.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HistoricoEmocional(
        List<Relato> relatos,
        List<Avaliacao> avaliacoes,
        List<Pensamento> pensamentos,
        List<Sono> sono,
        List<Atividade> atividades
) {
    public HistoricoEmocional {
        relatos = List.copyOf(relatos);
        avaliacoes = List.copyOf(avaliacoes);
        pensamentos = List.copyOf(pensamentos);
        sono = List.copyOf(sono);
        atividades = List.copyOf(atividades);
    }

    public record Relato(LocalDate data, String titulo, String conteudo) {
    }

    public record SentimentoRegistrado(String nome, Integer intensidade) {
    }

    public record Avaliacao(
            LocalDate data,
            Integer nota,
            String textoLivre,
            List<String> gatilhos,
            List<SentimentoRegistrado> sentimentos
    ) {
        public Avaliacao {
            gatilhos = List.copyOf(gatilhos);
            sentimentos = List.copyOf(sentimentos);
        }
    }

    public record Pensamento(
            LocalDate data,
            Integer intensidade,
            String situacao,
            String pensamento,
            List<String> emocoes
    ) {
        public Pensamento {
            emocoes = List.copyOf(emocoes);
        }
    }

    public record Sono(
            LocalDate data,
            LocalTime horaDormir,
            LocalTime horaAcordar,
            long duracaoMinutos,
            String qualidade,
            String disposicao,
            String comentario
    ) {
    }

    public record Atividade(
            String titulo,
            String subtitulo,
            LocalTime horaInicio,
            LocalTime horaFim,
            List<String> diasDaSemana
    ) {
        public Atividade {
            diasDaSemana = List.copyOf(diasDaSemana);
        }
    }
}
