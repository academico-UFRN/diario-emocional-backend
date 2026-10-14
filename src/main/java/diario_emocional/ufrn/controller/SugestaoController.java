package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.entity.Solicitacao;
import diario_emocional.ufrn.service.IAService;
import diario_emocional.ufrn.service.SugestaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/sugestao-IA/saude-mental")
public class SugestaoController {
    private final IAService iaService;
    private final SugestaoService sugestaoService;

    public SugestaoController(IAService iaService, SugestaoService sugestaoService){
        this.sugestaoService = sugestaoService;
        this.iaService = iaService;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Solicitacao> sugerirOpcaoSaudeMental(
            @PathVariable Long usuarioId) throws IOException, InterruptedException {

        Solicitacao solicitacao = this.sugestaoService.criarSolicitacao(usuarioId, LocalDate.now());

        return ResponseEntity.status(HttpStatus.OK).body(solicitacao);
    }
}
