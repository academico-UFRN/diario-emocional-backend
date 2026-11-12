package diario_emocional.ufrn.instancia_estudantes.dto;

import diario_emocional.ufrn.framework.dto.SentimentoDTO;
import diario_emocional.ufrn.framework.entity.Usuario;

import java.time.LocalDate;
import java.util.List;

public record AvaliacaoEstudanteResponseDTO(
        LocalDate dataRegistro,
        Integer avaliacaoDia,
        List<SentimentoDTO> sentimentos,
        List<String> gatilhos,
        Usuario usuario,
        String textoLivre
) {}
