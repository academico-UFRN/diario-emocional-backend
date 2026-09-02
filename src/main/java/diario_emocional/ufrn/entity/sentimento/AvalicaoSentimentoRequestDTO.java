package diario_emocional.ufrn.entity.sentimento;

import java.util.List;

public record AvalicaoSentimentoRequestDTO(
        Integer avaliacaoDia,
        List<SentimentoDTO> sentimentos,
        List<String> gatilhos,
        String textoLivre
) {}
