package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.service.RelatoDiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/relato")
public class RelatoDiaController {
    private final RelatoDiaService relatoDiaService;

    public RelatoDiaController(RelatoDiaService relatoDiaService) {
        this.relatoDiaService = relatoDiaService;
    }

    @PostMapping("/criar/{usuarioId}")
    public ResponseEntity<RelatoDia> criarRelatoDoDia(
            @PathVariable Long usuarioId,
            @RequestBody RelatoDia relato){

        RelatoDia response = relatoDiaService.criar(relato, usuarioId);

        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/buscarEspecifico/{usuarioId}/{relatoId}")
    public ResponseEntity<RelatoDia> buscarRelatoDoDia(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate relatoId){

        RelatoDia response = relatoDiaService.retornarRelatoEspecificoPorUsuario(relatoId,usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/buscarVarios/{usuarioId}")
    public ResponseEntity<List<RelatoDia>> buscarRelatosDoUsuario(
            @PathVariable Long usuarioId){
        List<RelatoDia> response = relatoDiaService.retornarRelatosPorUsuario(usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /*
    @GetMapping("/IA/sugestoes/{usuarioId}")
    public ResponseEntity<String> sugerirEscritaDeRelato(
            @PathVariable Long usuarioId){

    }

     */



}
