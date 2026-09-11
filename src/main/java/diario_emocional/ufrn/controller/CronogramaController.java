package diario_emocional.ufrn.controller; 

import diario_emocional.ufrn.dto.AtividadeResponseDTO;
import diario_emocional.ufrn.dto.CronogramaResponseDTO;
import diario_emocional.ufrn.enums.DiaSemana;
import diario_emocional.ufrn.service.AtividadeObrigatoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cronograma")
public class CronogramaController {

    private final AtividadeObrigatoriaService service;

    public CronogramaController(AtividadeObrigatoriaService service) {
        this.service = service;
    }

    // GET cronograma
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CronogramaResponseDTO> obterCronogramaCompleto(@PathVariable Long usuarioId) {
        CronogramaResponseDTO cronograma = service.obterCronogramaDoUsuario(usuarioId);
        return ResponseEntity.ok(cronograma);
    }

    // GET dia específico
    @GetMapping("/usuario/{usuarioId}/dia/{dia}")
    public ResponseEntity<List<AtividadeResponseDTO>> obterPorDia(
            @PathVariable Long usuarioId,
            @PathVariable DiaSemana dia) {
        List<AtividadeResponseDTO> atividades = service.obterAtividadesPorDia(usuarioId, dia);
        return ResponseEntity.ok(atividades);
    }

    // GET semana específica
    @GetMapping("/usuario/{usuarioId}/semana/{dataInicio}")
    public ResponseEntity<CronogramaResponseDTO> obterPorSemana(
            @PathVariable Long usuarioId,
            @PathVariable String dataInicio) {
        CronogramaResponseDTO cronograma = service.obterCronogramaDoUsuario(usuarioId);
        return ResponseEntity.ok(cronograma);
    }
}