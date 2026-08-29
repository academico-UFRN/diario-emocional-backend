package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.service.RelatoDiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller("/relato")
public class RelatoDiaController {
    RelatoDiaService relatoDiaService;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<RelatoDia> criarRelatoDoDia(
            @PathVariable Long usuarioId,
            @RequestBody RelatoDia relato){

        RelatoDia response = relatoDiaService.criar(relato, usuarioId);

        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
