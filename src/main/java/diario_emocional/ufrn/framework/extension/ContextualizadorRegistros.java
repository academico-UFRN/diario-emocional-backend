package diario_emocional.ufrn.framework.extension;

import diario_emocional.ufrn.framework.model.ContextoRegistros;
import diario_emocional.ufrn.framework.model.HistoricoEmocional;

import java.time.LocalDate;

public interface ContextualizadorRegistros {
    ContextoRegistros contextualizar(
            Long usuarioId,
            LocalDate dataReferencia,
            HistoricoEmocional historico
    );
}
