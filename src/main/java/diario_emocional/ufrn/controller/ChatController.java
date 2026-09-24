package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.IA.ChatRequestDTO;
import diario_emocional.ufrn.dto.IA.ChatResponseDTO;
import diario_emocional.ufrn.dto.IA.ChatResumoDTO;
import diario_emocional.ufrn.entity.Chat;
import diario_emocional.ufrn.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {

    ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ai/criar/{usuarioId}")
    public ResponseEntity<ChatResponseDTO> Criar(@PathVariable Long usuarioId, @RequestBody ChatRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.criar(dto.mensagem(), usuarioId));
    }

    @PostMapping("/ai/{chatId}/{usuarioId}")
    public ResponseEntity<ChatResponseDTO> Chat(@PathVariable Long usuarioId, @PathVariable UUID chatId, @RequestBody ChatRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.chat(dto.mensagem(), chatId, usuarioId));
    }

    @GetMapping("/ai/{usuarioId}")
    public ResponseEntity<List<ChatResumoDTO>> Buscar(@PathVariable Long usuarioId) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.buscar(usuarioId));
    }

    @GetMapping("/ai/chat/{chatId}")
    public ResponseEntity<ChatResponseDTO> Chat(@PathVariable UUID chatId) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.buscarUnico(chatId));
    }
}
