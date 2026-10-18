package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.entity.Solicitacao;
import diario_emocional.ufrn.service.IAService;
import diario_emocional.ufrn.service.SugestaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sugestao-saude-mental")
public class SugestaoController {
    private final IAService iaService;
    private final SugestaoService sugestaoService;

    public SugestaoController(IAService iaService, SugestaoService sugestaoService){
        this.sugestaoService = sugestaoService;
        this.iaService = iaService;
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<Solicitacao> sugerirOpcaoSaudeMental(
            @PathVariable Long usuarioId) throws IOException, InterruptedException {

        Solicitacao solicitacao = this.sugestaoService.criarSolicitacao(usuarioId, LocalDate.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacao);
    }

    @GetMapping("/buscarVarios/{usuarioId}")
    public ResponseEntity<List<Solicitacao>> listarOpcoesDeSaudeMental(
            @PathVariable Long usuarioId) {

        List<Solicitacao> solicitacoes = this.sugestaoService.buscarVariasSolicitacoes(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(solicitacoes);

    }

    @GetMapping("/buscarEspecifico/{usuarioId}/{solicitacaoId}")
    public ResponseEntity<Solicitacao> resgatarOpcaoIndividual(
            @PathVariable Long usuarioId,
            @PathVariable Long solicitacaoId
    ){
        Solicitacao solicitacao = this.sugestaoService.buscarSolicitacaoEspecifica(usuarioId, solicitacaoId);

        return ResponseEntity.status(HttpStatus.OK).body(solicitacao);
    }
}
