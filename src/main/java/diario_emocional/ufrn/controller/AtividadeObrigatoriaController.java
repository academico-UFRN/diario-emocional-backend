package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.AtividadeRequestDTO;
import diario_emocional.ufrn.dto.AtividadeResponseDTO;
import diario_emocional.ufrn.dto.CronogramaResponseDTO;
import diario_emocional.ufrn.service.AtividadeObrigatoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/api/atividades-obrigatorias")
public class AtividadeObrigatoriaController {
    private final AtividadeObrigatoriaService service;

    public AtividadeObrigatoriaController(AtividadeObrigatoriaService service) {
        this.service = service;
    }

    // POST criar nova atividade
    @PostMapping("/{usuarioId}")
    public ResponseEntity<AtividadeResponseDTO> criar(
            @PathVariable("usuarioId") Long usuarioId,
            @Valid @RequestBody AtividadeRequestDTO dto) {
        AtividadeResponseDTO novaAtividade = service.criarAtividade(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAtividade);
    }

    // GET listar todas
    @GetMapping("/{usuarioId}")
    public ResponseEntity<CronogramaResponseDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        CronogramaResponseDTO cronograma = service.obterCronogramaDoUsuario(usuarioId);
        return ResponseEntity.ok(cronograma);
    }

    //GET obter detalhes de uma atividade
    @GetMapping("/{usuarioId}/{id}")
    public ResponseEntity<AtividadeResponseDTO> buscarPorId(
            @PathVariable Long usuarioId,
            @PathVariable Long id) {
        AtividadeResponseDTO atividade = service.obterPorIdEUsuario(usuarioId, id);
        return ResponseEntity.ok(atividade);
    }

    //PUT atualizar
    @PutMapping("/{usuarioId}/{id}")
    public ResponseEntity<AtividadeResponseDTO> atualizar(
            @PathVariable("usuarioId") Long usuarioId,
            @PathVariable("id") Long id,
            @Valid @RequestBody AtividadeRequestDTO dto) {
        AtividadeResponseDTO atualizada = service.atualizarAtividade(usuarioId, id, dto);
        return ResponseEntity.ok(atualizada);
    }

    //DELETE
    @DeleteMapping("/{usuarioId}/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long usuarioId,
            @PathVariable Long id) {
        service.deletarAtividade(usuarioId, id);
        return ResponseEntity.noContent().build();
    }
}
