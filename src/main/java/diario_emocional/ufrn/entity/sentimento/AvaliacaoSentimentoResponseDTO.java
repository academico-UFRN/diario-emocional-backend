package diario_emocional.ufrn.entity.sentimento;


import diario_emocional.ufrn.entity.Usuario;
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
    public AvaliacaoSentimentoResponseDTO(AvaliacaoSentimento entity) {
        this(
                entity.getDataRegistro(),
                entity.getAvaliacaoDia(),
                entity.getSentimentos() != null ?
                        entity.getSentimentos().stream()
                                .map(s -> new SentimentoDTO(s.getSentimento(), s.getIntesidade()))
                                .toList()
                        : List.of(),
                entity.getGatilhos(),
                entity.getUsuario(),
                entity.getTextoLivre()

        );
    }
}