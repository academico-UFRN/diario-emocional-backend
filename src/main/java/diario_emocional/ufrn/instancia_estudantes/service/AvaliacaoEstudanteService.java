package diario_emocional.ufrn.instancia_estudantes.service;

import diario_emocional.ufrn.framework.dto.SentimentoDTO;
import diario_emocional.ufrn.framework.entity.Sentimento;
import diario_emocional.ufrn.framework.exception.ResourceNotFoundException;
import diario_emocional.ufrn.framework.repository.AvaliacaoBaseRepository;
import diario_emocional.ufrn.framework.repository.UsuarioRepository;
import diario_emocional.ufrn.framework.service.AvaliacaoService;
import diario_emocional.ufrn.framework.service.StreakPeriodService;
import diario_emocional.ufrn.instancia_estudantes.dto.AvaliacaoEstudanteRequestDTO;
import diario_emocional.ufrn.instancia_estudantes.dto.AvaliacaoEstudanteResponseDTO;
import diario_emocional.ufrn.instancia_estudantes.entity.AvaliacaoEstudante;
import diario_emocional.ufrn.instancia_estudantes.repository.AvaliacaoEstudanteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class AvaliacaoEstudanteService extends AvaliacaoService<AvaliacaoEstudante, AvaliacaoEstudanteRequestDTO, AvaliacaoEstudanteResponseDTO> {

    private final AvaliacaoEstudanteRepository avaliacaoEstudanteRepository;

    public AvaliacaoEstudanteService(
            AvaliacaoEstudanteRepository avaliacaoEstudanteRepository,
            UsuarioRepository usuarioRepository,
            StreakPeriodService streakPeriodService
    ) {
        super(usuarioRepository, streakPeriodService);
        this.avaliacaoEstudanteRepository = avaliacaoEstudanteRepository;
    }

    @Override
    protected AvaliacaoBaseRepository<AvaliacaoEstudante> getRepository() {
        return avaliacaoEstudanteRepository;
    }

    @Override
    protected AvaliacaoEstudanteResponseDTO convertToDTO(AvaliacaoEstudante entity) {
        return new AvaliacaoEstudanteResponseDTO(
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

    @Override
    protected AvaliacaoEstudante convertToEntity(AvaliacaoEstudanteRequestDTO dto) {
        List<Sentimento> sentimentos = (dto.sentimentos() != null)
                ? dto.sentimentos().stream()
                        .map(s -> new Sentimento(s.sentimento(), s.intensidade()))
                        .toList()
                : Collections.emptyList();

        return new AvaliacaoEstudante(
                dto.avaliacaoDia(),
                dto.textoLivre(),
                dto.gatilhos(),
                sentimentos
        );
    }

    @Override
    protected AvaliacaoEstudante atualizarCampos(AvaliacaoEstudante existente, AvaliacaoEstudante dadosNovos) {
        existente.setAvaliacaoDia(dadosNovos.getAvaliacaoDia());
        existente.setTextoLivre(dadosNovos.getTextoLivre());
        existente.setGatilhos(dadosNovos.getGatilhos());
        existente.setSentimentos(dadosNovos.getSentimentos());
        return existente;
    }

    @Override
    protected RuntimeException notFoundException(LocalDate dataRegistro) {
        return new ResourceNotFoundException("Avaliação não encontrada com data: " + dataRegistro);
    }

    @Override
    protected void validarEspecifico(AvaliacaoEstudante entity) {
        if (entity.getSentimentos() == null || entity.getSentimentos().isEmpty()) {
            throw new IllegalArgumentException("É necessário 1 ou mais sentimentos");
        }
    }
}
