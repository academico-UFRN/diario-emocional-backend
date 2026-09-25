package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.IA.IAResponseCriarDTO;
import diario_emocional.ufrn.dto.IA.ChatResponseDTO;
import diario_emocional.ufrn.dto.IA.ChatResumoDTO;
import diario_emocional.ufrn.dto.IA.IAResponseDTO;
import diario_emocional.ufrn.entity.Chat;
import diario_emocional.ufrn.entity.Mensagem;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.enums.MensagemRole;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.mapper.ChatMapper;
import diario_emocional.ufrn.repository.ChatRepository;
import diario_emocional.ufrn.repository.MensagemRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    ChatRepository chatRepository;
    MensagemRepository mensagemRepository;
    UsuarioRepository usuarioRepository;
    IAService iaService;
    ChatMapper chatMapper;

    @Autowired
    public ChatService(ChatRepository chatRepository, IAService iaService, MensagemRepository mensagemRepository, UsuarioRepository usuarioRepository, ChatMapper chatMapper) {
        this.chatRepository = chatRepository;
        this.iaService = iaService;
        this.mensagemRepository = mensagemRepository;
        this.usuarioRepository = usuarioRepository;
        this.chatMapper = chatMapper;
    }

    @Transactional
    public ChatResponseDTO criar(String mensagem, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        IAResponseCriarDTO resposta = iaService.PrimeiraResposta(mensagem);

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

        Chat chat = chatRepository.findByIdAndUsuario(chatId, usuario).orElseThrow(() -> new ResourceNotFoundException("Chat não encontrado com o ID: " + chatId));

        List<Mensagem> listaDeMensagens = mensagemRepository.findTop10ByChatIdOrderByCriadoEmDesc(chatId);

        IAResponseDTO resposta =  iaService.chat(mensagem, chatId, listaDeMensagens);

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
