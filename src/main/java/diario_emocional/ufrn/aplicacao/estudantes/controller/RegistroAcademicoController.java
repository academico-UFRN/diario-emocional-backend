package diario_emocional.ufrn.aplicacao.estudantes.controller;

import diario_emocional.ufrn.aplicacao.estudantes.dominio.RegistroAcademico;
import diario_emocional.ufrn.aplicacao.estudantes.dto.RegistroAcademicoRequest;
import diario_emocional.ufrn.aplicacao.estudantes.service.RegistroAcademicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academico/{usuarioId}/registros")
public class RegistroAcademicoController {
    private final RegistroAcademicoService service;

    public RegistroAcademicoController(RegistroAcademicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RegistroAcademico> criar(
            @PathVariable Long usuarioId,
            @Valid @RequestBody RegistroAcademicoRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(usuarioId, request));
    }

    @GetMapping
    public ResponseEntity<List<RegistroAcademico>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listar(usuarioId));
    }

    @GetMapping("/{registroId}")
    public ResponseEntity<RegistroAcademico> buscar(
            @PathVariable Long usuarioId,
            @PathVariable Long registroId
    ) {
        return ResponseEntity.ok(service.buscar(usuarioId, registroId));
    }

    @PutMapping("/{registroId}")
    public ResponseEntity<RegistroAcademico> atualizar(
            @PathVariable Long usuarioId,
            @PathVariable Long registroId,
            @Valid @RequestBody RegistroAcademicoRequest request
    ) {
        return ResponseEntity.ok(service.atualizar(usuarioId, registroId, request));
    }

    @DeleteMapping("/{registroId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long usuarioId,
            @PathVariable Long registroId
    ) {
        service.deletar(usuarioId, registroId);
        return ResponseEntity.noContent().build();
    }
}
