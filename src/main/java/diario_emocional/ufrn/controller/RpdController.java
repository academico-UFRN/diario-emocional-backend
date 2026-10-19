package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.RpdRequestDTO;
import diario_emocional.ufrn.dto.RpdResponseDTO;
import diario_emocional.ufrn.service.RpdService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rpd/{usuarioId}")
public class RpdController {
    private final RpdService rpdService;

    public RpdController(RpdService rpdService) {
        this.rpdService = rpdService;
    }

    @PostMapping
    public ResponseEntity<RpdResponseDTO> criar(
            @PathVariable Long usuarioId,
            @Valid @RequestBody RpdRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rpdService.criar(usuarioId, dto));
    }

    @GetMapping
    public ResponseEntity<List<RpdResponseDTO>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(rpdService.listar(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RpdResponseDTO> buscar(
            @PathVariable Long usuarioId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(rpdService.buscar(usuarioId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RpdResponseDTO> editar(
            @PathVariable Long usuarioId,
            @PathVariable Long id,
            @Valid @RequestBody RpdRequestDTO dto
    ) {
        return ResponseEntity.ok(rpdService.editar(usuarioId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long usuarioId, @PathVariable Long id) {
        rpdService.deletar(usuarioId, id);
        return ResponseEntity.noContent().build();
    }
}
