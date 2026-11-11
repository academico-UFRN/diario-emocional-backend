package diario_emocional.ufrn.service;

import diario_emocional.ufrn.entity.Solicitacao;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.framework.service.GeracaoBemEstarService;
import diario_emocional.ufrn.repository.SolicitacaoRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class SugestaoService {

    private final GeracaoBemEstarService geracaoBemEstarService;
    private final SolicitacaoRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public SugestaoService(
            GeracaoBemEstarService geracaoBemEstarService,
            SolicitacaoRepository solicitacaoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.geracaoBemEstarService = geracaoBemEstarService;
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Solicitacao criarSolicitacao(Long usuarioId, LocalDate data)
            throws IOException, InterruptedException {
        return geracaoBemEstarService.gerar(usuarioId, data);
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
