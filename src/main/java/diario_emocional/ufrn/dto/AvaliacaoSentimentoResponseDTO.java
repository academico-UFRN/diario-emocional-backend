package diario_emocional.ufrn.dto;


import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.entity.AvaliacaoSentimento;

import java.time.LocalDate;
import java.util.List;

// todos os campos são implicitamente private final
public record AvaliacaoSentimentoResponseDTO(
        LocalDate dataRegistro,
        Integer avaliacaoDia,
        List<SentimentoDTO> sentimentos,
        List<String> gatilhos,

        Usuario usuario,
        String textoLivre
) {
}