package diario_emocional.ufrn.framework.service;

import diario_emocional.ufrn.framework.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.framework.dto.RespostasSugestaoDTO;
import diario_emocional.ufrn.framework.dto.SugestaoDTO;
import diario_emocional.ufrn.framework.entity.*;
import diario_emocional.ufrn.framework.exception.ResourceNotFoundException;
import diario_emocional.ufrn.framework.repository.*;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SugestaoService {

    private final IAService iaService;
    private final ObjectMapper objectMapper=new ObjectMapper();
    private final SolicitacaoRepository solicitacaoRepository;
    private final RelatoDiaRepository relatoDiaRepository;
    private final RelatoHigieneSonoRepository relatoHigieneSonoRepository;
    private final AtividadeObrigatoriaRepository atividadeObrigatoriaRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;

    SugestaoService(IAService iaService, SolicitacaoRepository solicitacaoRepository, RelatoDiaRepository relatoDiaRepository,RelatoHigieneSonoRepository relatoHigieneSonoRepository,AtividadeObrigatoriaRepository atividadeObrigatoriaRepository, AvaliacaoRepository avaliacaoRepository, UsuarioRepository usuarioRepository){
        this.iaService = iaService;
        this.solicitacaoRepository = solicitacaoRepository;
        this.relatoDiaRepository = relatoDiaRepository;
        this.relatoHigieneSonoRepository = relatoHigieneSonoRepository;
        this.atividadeObrigatoriaRepository = atividadeObrigatoriaRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
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

        List<Avaliacao> avaliacao7dias = this.avaliacaoRepository.findAllByUsuario_IdAndDataRegistroBetween(usuarioId, data.minusDays(7), data);

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

        RespostasSugestaoDTO respostas = objectMapper.readValue(responseString, RespostasSugestaoDTO.class);
        Solicitacao solicitacao = new Solicitacao();
        List<Sugestao> listaSugestao = new ArrayList<>();

        for (SugestaoDTO sugestao : respostas.getSugestoes()) {

            Sugestao sugestaoResposta = new Sugestao(
                    sugestao.getAcao(),
                    sugestao.getMotivo()
            );

            // Define a solicitação à qual a sugestão pertence
            sugestaoResposta.setSolicitacao(solicitacao);

            listaSugestao.add(sugestaoResposta);
        }

        solicitacao.setSugestoes(listaSugestao);
        solicitacao.setDataRegistro(LocalDate.now());

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        solicitacao.setUsuario(usuario);

        solicitacaoRepository.save(solicitacao);

        return solicitacao;
    }

    public List<Solicitacao> buscarVariasSolicitacoes(Long usuarioId){
        return this.solicitacaoRepository.findAllByUsuario_Id(usuarioId);
    }

    public Solicitacao buscarSolicitacaoEspecifica(Long usuarioId, Long soliticacaoId){
        // verifica se o usuário existe
        Usuario usuario = validaUsuarioExistente(usuarioId);

        // verifica se usuario + solicitação já existem
        if (!this.solicitacaoRepository.existsByUsuario_IdAndId(usuarioId, soliticacaoId)) {
            throw new ResourceNotFoundException("Não foi possível encontrar essa soliticitação de saúde mental para esse usuário");
        } else {
            return this.solicitacaoRepository.findByUsuario_IdAndId(usuarioId, soliticacaoId);
        }
    }

    private Usuario validaUsuarioExistente(Long usuarioId) {
        Usuario usuario = this.usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("O usuário responsável não existe.")
                );
        return usuario;
    }
}
