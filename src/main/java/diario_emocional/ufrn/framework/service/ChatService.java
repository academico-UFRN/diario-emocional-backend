package diario_emocional.ufrn.framework.service;

import diario_emocional.ufrn.framework.dto.IA.IAResponseCriarDTO;
import diario_emocional.ufrn.framework.dto.IA.ChatResponseDTO;
import diario_emocional.ufrn.framework.dto.IA.ChatResumoDTO;
import diario_emocional.ufrn.framework.dto.IA.IAResponseDTO;
import diario_emocional.ufrn.framework.entity.Avaliacao;
import diario_emocional.ufrn.framework.entity.Chat;
import diario_emocional.ufrn.framework.entity.Mensagem;
import diario_emocional.ufrn.framework.entity.Usuario;
import diario_emocional.ufrn.framework.enums.MensagemRole;
import diario_emocional.ufrn.framework.exception.ResourceNotFoundException;
import diario_emocional.ufrn.framework.mapper.ChatMapper;
import diario_emocional.ufrn.framework.repository.AvaliacaoRepository;
import diario_emocional.ufrn.framework.repository.ChatRepository;
import diario_emocional.ufrn.framework.repository.MensagemRepository;
import diario_emocional.ufrn.framework.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    ChatRepository chatRepository;
    MensagemRepository mensagemRepository;
    UsuarioRepository usuarioRepository;
    AvaliacaoRepository avaliacaoRepository;
    IAService iaService;
    ChatMapper chatMapper;

    public ChatService(ChatRepository chatRepository, IAService iaService, MensagemRepository mensagemRepository,
            UsuarioRepository usuarioRepository, AvaliacaoRepository avaliacaoRepository, ChatMapper chatMapper) {
        this.chatRepository = chatRepository;
        this.iaService = iaService;
        this.mensagemRepository = mensagemRepository;
        this.usuarioRepository = usuarioRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.chatMapper = chatMapper;
    }

    @Transactional
    public ChatResponseDTO criar(String mensagem, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        List<Avaliacao> ultimasAvaliacoes = avaliacaoRepository.findTop3ByUsuarioOrderByDataRegistroDesc(usuario);

        IAResponseCriarDTO resposta = iaService.gerarPrimeiraRespostaNoChatSocratico(mensagem, ultimasAvaliacoes);

        Chat chat = new Chat(resposta.titulo(), usuario);

        Mensagem mensagem_user = new Mensagem(MensagemRole.USER, mensagem, chat);
        Mensagem mensagem_ia = new Mensagem(MensagemRole.IA, resposta.mensagem(), chat);

        chat.addMessage(mensagem_user);
        chat.addMessage(mensagem_ia);

        return chatMapper.toDTO(chatRepository.save(chat));
    }

    @Transactional
    public ChatResponseDTO chat(String mensagem, UUID chatId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        Chat chat = chatRepository.findByIdAndUsuario(chatId, usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Chat não encontrado com o ID: " + chatId));

        List<Mensagem> listaDeMensagens = mensagemRepository.findTop10ByChatIdOrderByCriadoEmDesc(chatId);
        List<Avaliacao> ultimasAvaliacoes = avaliacaoRepository.findTop3ByUsuarioOrderByDataRegistroDesc(usuario);

        IAResponseDTO resposta = iaService.gerarRespostaNoChatSocratico(mensagem, listaDeMensagens, ultimasAvaliacoes);

        Mensagem mensagem_user = new Mensagem(MensagemRole.USER, mensagem, chat);
        Mensagem mensagem_ia = new Mensagem(MensagemRole.IA, resposta.mensagem(), chat);

        chat.addMessage(mensagem_user);
        chat.addMessage(mensagem_ia);

        return chatMapper.toDTO(chatRepository.save(chat));
    }

    public List<ChatResumoDTO> buscar(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        return chatRepository.findResumoByUsuario(usuario);
    }

    public ChatResponseDTO buscarUnico(UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat não encontrado com o ID: " + chatId));

        return chatMapper.toDTO(chat);
    }

    @Transactional
    public void deletar(UUID chatId) {
        chatRepository.deleteById(chatId);
    }
}
