package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.dto.RespostasSugestaoDTO;
import diario_emocional.ufrn.dto.SugestaoDTO;
import diario_emocional.ufrn.entity.*;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.repository.*;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SugestaoService {

    private final IAService iaService;
    private final ObjectMapper objectMapper=new ObjectMapper();
    private final SolicitacaoRepository solicitacaoRepository;
    private final RelatoDiaRepository relatoDiaRepository;
    private final RelatoHigieneSonoRepository relatoHigieneSonoRepository;
    private final AtividadeObrigatoriaRepository atividadeObrigatoriaRepository;
    private final AvalicaoSentimentoRepository avalicaoSentimentoRepository;

    SugestaoService(IAService iaService, SolicitacaoRepository solicitacaoRepository, RelatoDiaRepository relatoDiaRepository,RelatoHigieneSonoRepository relatoHigieneSonoRepository,AtividadeObrigatoriaRepository atividadeObrigatoriaRepository, AvalicaoSentimentoRepository avalicaoSentimentoRepository){
        this.iaService = iaService;
        this.solicitacaoRepository = solicitacaoRepository;
        this.relatoDiaRepository = relatoDiaRepository;
        this.relatoHigieneSonoRepository = relatoHigieneSonoRepository;
        this.atividadeObrigatoriaRepository = atividadeObrigatoriaRepository;
        this.avalicaoSentimentoRepository = avalicaoSentimentoRepository;
    }

    public String criarPromptComContexto(Long usuarioId, LocalDate data){
        List<String> erros = new ArrayList<>();

        LocalDate dataInicial = data.minusDays(6);

        List<RelatoDia> relatos7dias = this.relatoDiaRepository.findAllByUsuarioIdAndDataRegistroBetween(usuarioId, dataInicial, data);

        if(relatos7dias.isEmpty()){
            erros.add("Não existe nenhum relato do dia nos últimos 7 dias.");
        }

        List<RelatoHigieneSono> higieneSono7dias = this.relatoHigieneSonoRepository.findAllByUsuario_IdAndDataRegistroBetween(usuarioId, dataInicial, data);

        if(higieneSono7dias.isEmpty()){
            erros.add("Não existe nenhum relato de higieno do sono nos últimos 7 dias.");
        }

        List<AtividadeObrigatoria> atividade7dias = atividadeObrigatoriaRepository.findAllByUsuario_IdAndAtivoTrue(usuarioId);

        if(atividade7dias.isEmpty()){
            erros.add("Não existe nenhuma atividade obrigatória cadastrada nos últimos 7 dias.");
        }

        List<AvaliacaoSentimento> avaliacao7dias = this.avalicaoSentimentoRepository.findAllByUsuario_IdAndDataRegistroBetween(usuarioId, data.minusDays(7), data);

        if(avaliacao7dias.isEmpty()){
            erros.add("Não existe nenhuma avaliação de sentimentos nos últimos 7 dias");
        }

        if (!erros.isEmpty()) {
            throw new ResourceNotFoundException(String.join(" ", erros));
        }

        return this.iaService.gerarPromptSugestaoSaudeMental(relatos7dias, higieneSono7dias, atividade7dias, avaliacao7dias);
    }

    public Solicitacao criarSolicitacao(Long usuarioId, LocalDate data) throws IOException, InterruptedException {
        String prompt = this.criarPromptComContexto(usuarioId, data);
        GeminiResponseDTO responseDTO = this.iaService.retornarSugestoesSaudeMental(prompt);
        String responseString = this.iaService.retornarStringSugestao(responseDTO);

        RespostasSugestaoDTO respostas = objectMapper.convertValue(responseString, RespostasSugestaoDTO.class);

        Solicitacao solicitacao = new Solicitacao();
        List<Sugestao> listaSugestao = new ArrayList<>();

        for(SugestaoDTO sugestao : respostas.getSugestoes()){
            Sugestao sugestaoResposta = new Sugestao(sugestao.getAcao(), sugestao.getMotivo());
            listaSugestao.add(sugestaoResposta);

        }

        solicitacao.setSugestoes(listaSugestao);
        solicitacao.setDataRegistro(LocalDate.now());

        solicitacaoRepository.save(solicitacao);

        return solicitacao;
    }

    public List<Solicitacao> listarSolicitacoes(Long usuarioId){
        return this.solicitacaoRepository.findAllByUsuario_Id(usuarioId);
    }
}
