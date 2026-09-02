package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.RelatoDiaEditarDto;
import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.service.RelatoDiaService;
import org.hibernate.annotations.Parameter;
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

        RelatoDia response = this.relatoDiaService.criar(relato, usuarioId);

        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/buscarEspecifico/{usuarioId}/{relatoId}")
    public ResponseEntity<RelatoDia> buscarRelatoDoDia(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate relatoId){

        RelatoDia response = this.relatoDiaService.retornarRelatoEspecificoPorUsuario(relatoId,usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/buscarVarios/{usuarioId}")
    public ResponseEntity<List<RelatoDia>> buscarRelatosDoUsuario(
            @PathVariable Long usuarioId){
        List<RelatoDia> response = this.relatoDiaService.retornarRelatosPorUsuario(usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /*
    @GetMapping("/IA/sugestoes/{usuarioId}")
    public ResponseEntity<String> sugerirEscritaDeRelato(
            @PathVariable Long usuarioId){

    }

     */

    @DeleteMapping("/deletar/{usuarioId}/{dataRegistro}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate dataRegistro
    ){
        this.relatoDiaService.deletar(dataRegistro, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/editar/{usuarioId}/{dataRegistro}")
    public ResponseEntity<RelatoDia> editar(
            @PathVariable Long usuarioId,
            @PathVariable LocalDate dataRegistro,
            @RequestBody RelatoDiaEditarDto relatoEditadoUsuario
    ){
        RelatoDia relatoEditado = this.relatoDiaService.editar(relatoEditadoUsuario, dataRegistro, usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(relatoEditado);
    }




}
