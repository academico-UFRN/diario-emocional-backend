package diario_emocional.ufrn.mapper;

import diario_emocional.ufrn.dto.AvaliacaoSentimentoRequestDTO;
import diario_emocional.ufrn.dto.AvaliacaoSentimentoResponseDTO;
import diario_emocional.ufrn.dto.SentimentoDTO;
import diario_emocional.ufrn.entity.AvaliacaoSentimento;
import diario_emocional.ufrn.entity.Sentimento;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoSentimentoMapper {
    public AvaliacaoSentimentoResponseDTO toDTO(AvaliacaoSentimento entity) {
        return new AvaliacaoSentimentoResponseDTO(
                entity.getDataRegistro(),
                entity.getAvaliacaoDia(),
                entity.getSentimentos().stream()
                        .map(s -> new SentimentoDTO(s.getSentimento(), s.getIntensidade()))
                        .toList(),
                entity.getGatilhos(),
                entity.getUsuario(),
                entity.getTextoLivre()
        );
    }

    public AvaliacaoSentimento toEntity(AvaliacaoSentimentoRequestDTO dto) {
        return new AvaliacaoSentimento(
                dto.avaliacaoDia(),
                dto.textoLivre(),
                dto.gatilhos(),
                dto.sentimentos().stream()
                        .map(s -> new Sentimento(s.sentimento(), s.intensidade()))
                        .toList()
        );
    }
}
