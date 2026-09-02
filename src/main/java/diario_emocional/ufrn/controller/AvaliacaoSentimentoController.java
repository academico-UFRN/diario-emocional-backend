package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.entity.sentimento.AvaliacaoSentimentoResponseDTO;
import diario_emocional.ufrn.entity.sentimento.AvalicaoSentimentoRequestDTO;
import diario_emocional.ufrn.service.AvaliacaoSentimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/avaliar-sentimentos")
public class AvaliacaoSentimentoController {
    private final AvaliacaoSentimentoService  avaliacaoSentimentoService;


    public AvaliacaoSentimentoController(AvaliacaoSentimentoService avaliacaoSentimentoService) {
        this.avaliacaoSentimentoService = avaliacaoSentimentoService;
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<AvaliacaoSentimentoResponseDTO> sentimentos(@PathVariable Long usuarioId, @RequestBody AvalicaoSentimentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.avaliacaoSentimentoService.criar(usuarioId, dto));
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<AvaliacaoSentimentoResponseDTO>>lista(@PathVariable Long usuarioId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.avaliacaoSentimentoService.listar(usuarioId));
    }

    @DeleteMapping("/{usuarioId}/{dataRegistro}")
    public ResponseEntity<Void> deletar(@PathVariable Long usuarioId,  @PathVariable LocalDate dataRegistro) {
        this.avaliacaoSentimentoService.deletar(dataRegistro, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{usuarioId}/{dataRegistro}")
    public ResponseEntity<AvaliacaoSentimentoResponseDTO> editar(@PathVariable Long usuarioId, @PathVariable LocalDate dataRegistro, @RequestBody AvalicaoSentimentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(this.avaliacaoSentimentoService.editar(dto, usuarioId, dataRegistro));
    }
}
