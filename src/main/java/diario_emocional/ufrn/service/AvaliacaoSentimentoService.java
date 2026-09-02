package diario_emocional.ufrn.service;

import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.entity.sentimento.AvaliacaoSentimento;
import diario_emocional.ufrn.entity.sentimento.AvaliacaoSentimentoResponseDTO;
import diario_emocional.ufrn.entity.sentimento.AvalicaoSentimentoRequestDTO;
import diario_emocional.ufrn.entity.sentimento.Sentimento;
import diario_emocional.ufrn.repository.AvalicaoSentimentoRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import jakarta.persistence.criteria.From;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvaliacaoSentimentoService {
    private final AvalicaoSentimentoRepository  avalicaoSentimentoRepository;
    private final UsuarioRepository usuarioRepository;

    AvaliacaoSentimentoService(AvalicaoSentimentoRepository avalicaoSentimentoRepository1, UsuarioRepository usuarioRepository) {
        this.avalicaoSentimentoRepository = avalicaoSentimentoRepository1;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AvaliacaoSentimentoResponseDTO criar(Long usuarioId, AvalicaoSentimentoRequestDTO dto) {

        LocalDate hoje = LocalDate.now();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));

        if (avalicaoSentimentoRepository.findByUsuarioAndDataRegistro(usuario, hoje).isPresent()) {
            throw new IllegalArgumentException("Já existe uma avaliação registrada para a data de hoje.");
        }


        AvaliacaoSentimento avaliacaoSentimento = new AvaliacaoSentimento();

        avaliacaoSentimento.setDataRegistro(hoje);
        avaliacaoSentimento.setUsuario(usuario);

        avaliacaoSentimento.From(dto);

        return new AvaliacaoSentimentoResponseDTO(this.avalicaoSentimentoRepository.save(avaliacaoSentimento));
    }
    public List<AvaliacaoSentimentoResponseDTO> listar(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));

        return this.avalicaoSentimentoRepository.findByUsuario(usuario).stream().map(avalicao -> {
            return new AvaliacaoSentimentoResponseDTO(avalicao);
        }).toList();
    }
    @Transactional
    public AvaliacaoSentimentoResponseDTO editar(AvalicaoSentimentoRequestDTO dto, Long usuarioId, LocalDate dataRegistro) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));

        AvaliacaoSentimento avaliacaoSentimento = this.avalicaoSentimentoRepository.findByUsuarioAndDataRegistro(usuario, dataRegistro).orElseThrow(
                () -> new RuntimeException("Avaliação não encontrada com data: " + dataRegistro)
        );

        avaliacaoSentimento.From(dto);

        return new AvaliacaoSentimentoResponseDTO(this.avalicaoSentimentoRepository.save(avaliacaoSentimento));
    }
    @Transactional
    public void deletar(LocalDate dataRegistro, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));

        AvaliacaoSentimento avaliacaoSentimento = this.avalicaoSentimentoRepository.findByUsuarioAndDataRegistro(usuario, dataRegistro).orElseThrow(
                () -> new RuntimeException("Avaliação não encontrada com data: " + dataRegistro)
        );

        this.avalicaoSentimentoRepository.delete(avaliacaoSentimento);
    }


}
