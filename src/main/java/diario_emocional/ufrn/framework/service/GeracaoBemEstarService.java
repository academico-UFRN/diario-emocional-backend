package diario_emocional.ufrn.framework.service;

import diario_emocional.ufrn.dto.SugestaoDTO;
import diario_emocional.ufrn.entity.Solicitacao;
import diario_emocional.ufrn.entity.Sugestao;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.framework.extension.AnalisadorPadroesEmocionais;
import diario_emocional.ufrn.framework.extension.ContextualizadorRegistros;
import diario_emocional.ufrn.framework.extension.EstrategiaSugestoesBemEstar;
import diario_emocional.ufrn.framework.model.AnalisePadroesEmocionais;
import diario_emocional.ufrn.framework.model.ContextoRegistros;
import diario_emocional.ufrn.framework.model.HistoricoEmocional;
import diario_emocional.ufrn.repository.SolicitacaoRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class GeracaoBemEstarService {
    private final UsuarioRepository usuarioRepository;
    private final HistoricoEmocionalService historicoService;
    private final ContextualizadorRegistros contextualizador;
    private final AnalisadorPadroesEmocionais analisador;
    private final EstrategiaSugestoesBemEstar estrategia;
    private final SolicitacaoRepository solicitacaoRepository;

    public GeracaoBemEstarService(
            UsuarioRepository usuarioRepository,
            HistoricoEmocionalService historicoService,
            ContextualizadorRegistros contextualizador,
            AnalisadorPadroesEmocionais analisador,
            EstrategiaSugestoesBemEstar estrategia,
            SolicitacaoRepository solicitacaoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.historicoService = historicoService;
        this.contextualizador = contextualizador;
        this.analisador = analisador;
        this.estrategia = estrategia;
        this.solicitacaoRepository = solicitacaoRepository;
    }

    public Solicitacao gerar(Long usuarioId, LocalDate data)
            throws IOException, InterruptedException {
        var usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o ID: " + usuarioId));

        HistoricoEmocional historico = historicoService.carregar(usuarioId, data);
        ContextoRegistros contexto =
                contextualizador.contextualizar(usuarioId, data, historico);
        AnalisePadroesEmocionais analise = analisador.analisar(historico, contexto);
        var sugestoesGeradas = estrategia.gerar(historico, contexto, analise);

        if (sugestoesGeradas == null || sugestoesGeradas.isEmpty()) {
            throw new IllegalStateException("A estratégia de bem-estar não gerou sugestões.");
        }

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setUsuario(usuario);
        solicitacao.setDataRegistro(data);

        var sugestoes = new ArrayList<Sugestao>();
        for (SugestaoDTO dto : sugestoesGeradas) {
            if (dto == null || dto.getAcao() == null || dto.getMotivo() == null) {
                throw new IllegalStateException(
                        "A estratégia retornou uma sugestão sem ação ou motivo.");
            }

            Sugestao sugestao = new Sugestao(dto.getAcao(), dto.getMotivo());
            sugestao.setSolicitacao(solicitacao);
            sugestoes.add(sugestao);
        }

        solicitacao.setSugestoes(sugestoes);
        return solicitacaoRepository.save(solicitacao);
    }
}
