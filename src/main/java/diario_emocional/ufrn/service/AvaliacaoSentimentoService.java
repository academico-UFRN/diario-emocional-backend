package diario_emocional.ufrn.service;

import diario_emocional.ufrn.entity.Sentimento;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.entity.AvaliacaoSentimento;
import diario_emocional.ufrn.dto.AvaliacaoSentimentoResponseDTO;
import diario_emocional.ufrn.dto.AvaliacaoSentimentoRequestDTO;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.mapper.AvaliacaoSentimentoMapper;
import diario_emocional.ufrn.repository.AvalicaoSentimentoRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvaliacaoSentimentoService {
    private final AvalicaoSentimentoRepository  avalicaoSentimentoRepository;
    private final AvaliacaoSentimentoMapper avaliacaoSentimentoMapper;

    private final UsuarioRepository usuarioRepository;

    AvaliacaoSentimentoService(AvalicaoSentimentoRepository avalicaoSentimentoRepository1, UsuarioRepository usuarioRepository, AvaliacaoSentimentoMapper avaliacaoSentimentoMapper) {
        this.avalicaoSentimentoRepository = avalicaoSentimentoRepository1;
        this.usuarioRepository = usuarioRepository;
        this.avaliacaoSentimentoMapper = avaliacaoSentimentoMapper;
    }

    @Transactional
    public AvaliacaoSentimentoResponseDTO criar(Long usuarioId, AvaliacaoSentimentoRequestDTO dto) {

        LocalDate data = LocalDate.now();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        avalicaoSentimentoRepository.findByUsuarioAndDataRegistro(usuario, data)
                .ifPresent(registro -> {
                    throw new IllegalArgumentException("Já possui um registro hoje!");
                });

        AvaliacaoSentimento entity = avaliacaoSentimentoMapper.toEntity(dto);

        entity.setDataRegistro(data);
        entity.setUsuario(usuario);

        return avaliacaoSentimentoMapper.toDTO(this.avalicaoSentimentoRepository.save(entity));
    }

    public List<AvaliacaoSentimentoResponseDTO> listar(Long usuarioId) {
        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

       return avalicaoSentimentoRepository.findByUsuario(usuario).stream().map(avaliacaoSentimentoMapper::toDTO).toList();
    }

    public AvaliacaoSentimentoResponseDTO get(Long usuarioId, LocalDate dataRegistro) {
        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        AvaliacaoSentimento avaliacaoSentimento = this.avalicaoSentimentoRepository
                .findByUsuarioAndDataRegistro(usuario, dataRegistro)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com data: " + dataRegistro));

        return avaliacaoSentimentoMapper.toDTO(avaliacaoSentimento);
    }

    @Transactional
    public AvaliacaoSentimentoResponseDTO editar(AvaliacaoSentimentoRequestDTO dto, Long usuarioId, LocalDate dataRegistro) {
        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        AvaliacaoSentimento avaliacaoSentimento = this.avalicaoSentimentoRepository
                .findByUsuarioAndDataRegistro(usuario, dataRegistro)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com data: " + dataRegistro));


        avaliacaoSentimento
                .atualizar(dto.avaliacaoDia(), dto.textoLivre(), dto.gatilhos(), dto.sentimentos()
                        .stream()
                        .map((s) -> new Sentimento(s.sentimento(), s.intensidade()))
                        .toList());

        return avaliacaoSentimentoMapper.toDTO(avaliacaoSentimento);
    }

    @Transactional
    public void deletar(LocalDate dataRegistro, Long usuarioId) {
        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        AvaliacaoSentimento avaliacaoSentimento = this.avalicaoSentimentoRepository
                .findByUsuarioAndDataRegistro(usuario, dataRegistro)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com data: " + dataRegistro));

        this.avalicaoSentimentoRepository.delete(avaliacaoSentimento);
    }


}
