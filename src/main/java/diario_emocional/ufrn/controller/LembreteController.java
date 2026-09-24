package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.entity.Lembrete;
import diario_emocional.ufrn.service.LembreteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lembrete")
public class LembreteController {

    private final LembreteService lembreteService;

    public LembreteController(LembreteService lembreteService) {
        this.lembreteService = lembreteService;
    }

    @GetMapping("/nao-enviados")
    public ResponseEntity<Lembrete> buscarNaoEnviados() {

        Lembrete lembrete = lembreteService.verificarLembreteParaEnviar();

        if (lembrete == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lembrete);
    }
}