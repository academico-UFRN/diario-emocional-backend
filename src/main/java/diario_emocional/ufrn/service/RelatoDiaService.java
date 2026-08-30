package diario_emocional.ufrn.service;

import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.repository.RelatoDiaRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RelatoDiaService {
    private final RelatoDiaRepository relatoDiaRepository;
    private final UsuarioRepository usuarioRepository;

    public RelatoDiaService(RelatoDiaRepository relatoDiaRepository, UsuarioRepository usuarioRepository){
        this.relatoDiaRepository = relatoDiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public RelatoDia criar(RelatoDia relato, Long usuarioId){

        // verifica se o relato vai ser criado em uma data futura
        if(relato.getDataRegistro().isAfter(LocalDate.now())){
            throw new RuntimeException("O relato não pode ser criado em uma data futura");
        }

        // verifica se usuario + relato já existem
        if(relatoDiaRepository.existsByUsuarioIdAndDataRegistro(usuarioId, relato.getDataRegistro())){
            throw new RuntimeException("O usuário já fez o relato do dia " + relato.getDataRegistro());
        }

        // acha o usuario pelo id para relacionar ao relato
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado")
                );

        relato.setUsuario(usuario);

        return relatoDiaRepository.save(relato);

    }

    /*
    public RelatoDia retornarRelato
 */
}
