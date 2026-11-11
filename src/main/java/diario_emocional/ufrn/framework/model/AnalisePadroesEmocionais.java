package diario_emocional.ufrn.framework.model;

import java.util.List;

public record AnalisePadroesEmocionais(
        List<Observacao> observacoes,
        List<String> limitacoes
) {
    public AnalisePadroesEmocionais {
        observacoes = List.copyOf(observacoes);
        limitacoes = List.copyOf(limitacoes);
    }

    public record Observacao(
            String criterio,
            String descricao,
            List<String> evidencias
    ) {
        public Observacao {
            evidencias = List.copyOf(evidencias);
        }
    }
}
