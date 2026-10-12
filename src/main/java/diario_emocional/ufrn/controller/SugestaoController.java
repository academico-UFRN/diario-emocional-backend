package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.service.IAService;
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

    public SugestaoController(IAService iaService){
        this.iaService = iaService;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<String> sugerirEscritaDeRelato(
            @PathVariable Long usuarioId) throws IOException, InterruptedException {

        String prompt = this.iaService.gerarPromptSugestaoSaudeMental(usuarioId, LocalDate.now());
        GeminiResponseDTO responseDTO = this.iaService.retornarSugestoesSaudeMental(prompt);
        String responseString = this.iaService.retornarStringSugestao(responseDTO);

        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }
}
