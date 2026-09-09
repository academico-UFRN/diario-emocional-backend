package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.AvaliacaoSentimentoResponseDTO;
import diario_emocional.ufrn.dto.AvaliacaoSentimentoRequestDTO;
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
    public ResponseEntity<AvaliacaoSentimentoResponseDTO> criar(@PathVariable Long usuarioId, @RequestBody AvaliacaoSentimentoRequestDTO dto) {
        AvaliacaoSentimentoResponseDTO avaliacaoSentimento = this.avaliacaoSentimentoService.criar(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoSentimento);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<AvaliacaoSentimentoResponseDTO>>lista(@PathVariable Long usuarioId) {
        List<AvaliacaoSentimentoResponseDTO> avaliacaoSentimentoList = this.avaliacaoSentimentoService.listar(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(avaliacaoSentimentoList);
    }

    @GetMapping("/{usuarioId}/{dataRegistro}")
    public ResponseEntity<AvaliacaoSentimentoResponseDTO> get(@PathVariable Long usuarioId, @PathVariable LocalDate dataRegistro) {
        AvaliacaoSentimentoResponseDTO avaliacaoSentimento = this.avaliacaoSentimentoService.get(usuarioId, dataRegistro);
        return ResponseEntity.status(HttpStatus.OK).body(avaliacaoSentimento);
    }

    @DeleteMapping("/{usuarioId}/{dataRegistro}")
    public ResponseEntity<Void> deletar(@PathVariable Long usuarioId,  @PathVariable LocalDate dataRegistro) {
        this.avaliacaoSentimentoService.deletar(dataRegistro, usuarioId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{usuarioId}/{dataRegistro}")
    public ResponseEntity<AvaliacaoSentimentoResponseDTO> editar(@PathVariable Long usuarioId, @PathVariable LocalDate dataRegistro, @RequestBody AvaliacaoSentimentoRequestDTO dto) {
        AvaliacaoSentimentoResponseDTO avaliacaoSentimento = this.avaliacaoSentimentoService.editar(dto, usuarioId, dataRegistro);
        return ResponseEntity.status(HttpStatus.OK).body(avaliacaoSentimento);
    }
}
