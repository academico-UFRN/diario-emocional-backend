package diario_emocional.ufrn.framework.extension;

import diario_emocional.ufrn.framework.model.AnalisePadroesEmocionais;
import diario_emocional.ufrn.framework.model.ContextoRegistros;
import diario_emocional.ufrn.framework.model.HistoricoEmocional;

import java.io.IOException;

public interface AnalisadorPadroesEmocionais {
    AnalisePadroesEmocionais analisar(
            HistoricoEmocional historico,
            ContextoRegistros contexto
    ) throws IOException, InterruptedException;
}
