package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.RelatoHigieneSonoRequestDTO;
import diario_emocional.ufrn.dto.RelatoHigieneSonoResponseDTO;
import diario_emocional.ufrn.service.RelatoHigieneSonoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/higiene-sono/{usuarioId}")
public class RelatoHigieneSonoController {
    private final RelatoHigieneSonoService service;

    public RelatoHigieneSonoController(RelatoHigieneSonoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RelatoHigieneSonoResponseDTO> criar(
            @PathVariable Long usuarioId,
            @Valid @RequestBody RelatoHigieneSonoRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(usuarioId, dto));
    }

    @GetMapping
    public ResponseEntity<List<RelatoHigieneSonoResponseDTO>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listar(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelatoHigieneSonoResponseDTO> buscar(
            @PathVariable Long usuarioId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscar(usuarioId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RelatoHigieneSonoResponseDTO> editar(
            @PathVariable Long usuarioId,
            @PathVariable Long id,
            @Valid @RequestBody RelatoHigieneSonoRequestDTO dto
    ) {
        return ResponseEntity.ok(service.editar(usuarioId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long usuarioId, @PathVariable Long id) {
        service.deletar(usuarioId, id);
        return ResponseEntity.noContent().build();
    }
}