package diario_emocional.ufrn.instancia_estudantes.controller;

import diario_emocional.ufrn.instancia_estudantes.dto.AvaliacaoEstudanteRequestDTO;
import diario_emocional.ufrn.instancia_estudantes.dto.AvaliacaoEstudanteResponseDTO;
import diario_emocional.ufrn.instancia_estudantes.service.AvaliacaoEstudanteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/avaliar-sentimentos")
public class AvaliacaoEstudanteController {

    private final AvaliacaoEstudanteService avaliacaoEstudanteService;

    public AvaliacaoEstudanteController(AvaliacaoEstudanteService avaliacaoEstudanteService) {
        this.avaliacaoEstudanteService = avaliacaoEstudanteService;
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<AvaliacaoEstudanteResponseDTO> criar(
            @PathVariable Long usuarioId,
            @RequestBody AvaliacaoEstudanteRequestDTO dto
    ) {
        AvaliacaoEstudanteResponseDTO avaliacao = this.avaliacaoEstudanteService.criar(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<AvaliacaoEstudanteResponseDTO>> lista(@PathVariable Long usuarioId) {
        List<AvaliacaoEstudanteResponseDTO> avaliacoes = this.avaliacaoEstudanteService.listar(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(avaliacoes);
    }

    @GetMapping("/{usuarioId}/{dataRegistro}")
    public ResponseEntity<AvaliacaoEstudanteResponseDTO> get(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate dataRegistro
    ) {
        AvaliacaoEstudanteResponseDTO avaliacao = this.avaliacaoEstudanteService.get(usuarioId, dataRegistro);
        return ResponseEntity.status(HttpStatus.OK).body(avaliacao);
    }

    @DeleteMapping("/{usuarioId}/{dataRegistro}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate dataRegistro
    ) {
        this.avaliacaoEstudanteService.deletar(dataRegistro, usuarioId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{usuarioId}/{dataRegistro}")
    public ResponseEntity<AvaliacaoEstudanteResponseDTO> editar(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate dataRegistro,
            @RequestBody AvaliacaoEstudanteRequestDTO dto
    ) {
        AvaliacaoEstudanteResponseDTO avaliacao = this.avaliacaoEstudanteService.editar(dto, usuarioId, dataRegistro);
        return ResponseEntity.status(HttpStatus.OK).body(avaliacao);
    }
}
