package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.dto.RelatoDiaEditarDto;
import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.service.IAService;
import diario_emocional.ufrn.service.RelatoDiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relato")
public class RelatoDiaController {
    private final RelatoDiaService relatoDiaService;
    private final IAService iaService;

    public RelatoDiaController(RelatoDiaService relatoDiaService, IAService iaService) {
        this.relatoDiaService = relatoDiaService;
        this.iaService = iaService;
    }

    @PostMapping("/criar/{usuarioId}")
    public ResponseEntity<RelatoDia> criarRelatoDoDia(
            @PathVariable Long usuarioId,
            @RequestBody RelatoDia relato)  {

        RelatoDia response = this.relatoDiaService.criar(relato, usuarioId);

        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/buscarEspecifico/{usuarioId}/{relatoId}")
    public ResponseEntity<RelatoDia> buscarRelatoDoDia(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate relatoId)  {

        RelatoDia response = this.relatoDiaService.retornarRelatoEspecificoPorUsuario(relatoId,usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/buscarVarios/{usuarioId}")
    public ResponseEntity<List<RelatoDia>> buscarRelatosDoUsuario(
            @PathVariable Long usuarioId) {
        List<RelatoDia> response = this.relatoDiaService.retornarRelatosPorUsuario(usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/IA-sugestao/{usuarioId}")
    public ResponseEntity<String> sugerirEscritaDeRelato(
            @PathVariable Long usuarioId) throws IOException, InterruptedException {

        String prompt = this.iaService.gerarPromptSugestoesRelatoDia(usuarioId, LocalDate.now());
        GeminiResponseDTO responseDTO = this.iaService.retornarSugestoesRelatoDia(prompt);
        String responseString = this.iaService.retornarStringSugestao(responseDTO);

        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }


    @DeleteMapping("/deletar/{usuarioId}/{dataRegistro}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate dataRegistro
    )  {
        this.relatoDiaService.deletar(dataRegistro, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/editar/{usuarioId}/{dataRegistro}")
    public ResponseEntity<RelatoDia> editar(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate dataRegistro,
            @RequestBody RelatoDiaEditarDto relatoEditadoUsuario
    ) {
        RelatoDia relatoEditado = this.relatoDiaService.editar(relatoEditadoUsuario, dataRegistro, usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(relatoEditado);
    }




}
