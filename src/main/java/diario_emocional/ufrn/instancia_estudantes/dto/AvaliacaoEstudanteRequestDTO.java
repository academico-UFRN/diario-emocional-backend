package diario_emocional.ufrn.instancia_estudantes.dto;

import diario_emocional.ufrn.framework.dto.SentimentoDTO;

import java.util.List;

public record AvaliacaoEstudanteRequestDTO(
        Integer avaliacaoDia,
        List<SentimentoDTO> sentimentos,
        List<String> gatilhos,
        String textoLivre
) {}
