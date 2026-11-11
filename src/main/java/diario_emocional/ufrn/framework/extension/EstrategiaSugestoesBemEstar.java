package diario_emocional.ufrn.framework.extension;

import diario_emocional.ufrn.dto.SugestaoDTO;
import diario_emocional.ufrn.framework.model.AnalisePadroesEmocionais;
import diario_emocional.ufrn.framework.model.ContextoRegistros;
import diario_emocional.ufrn.framework.model.HistoricoEmocional;

import java.io.IOException;
import java.util.List;

public interface EstrategiaSugestoesBemEstar {
    List<SugestaoDTO> gerar(
            HistoricoEmocional historico,
            ContextoRegistros contexto,
            AnalisePadroesEmocionais analise
    ) throws IOException, InterruptedException;
}
