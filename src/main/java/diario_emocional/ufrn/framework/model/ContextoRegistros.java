package diario_emocional.ufrn.framework.model;

import java.util.List;

public record ContextoRegistros(
        String orientacao,
        List<String> temasRelevantes,
        ContextoAplicacao dadosDaAplicacao
) {
    public ContextoRegistros {
        temasRelevantes = List.copyOf(temasRelevantes);
        if (dadosDaAplicacao == null) {
            throw new IllegalArgumentException("Os dados específicos da aplicação são obrigatórios.");
        }
    }
}
