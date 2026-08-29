package diario_emocional.ufrn.service;

import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.repository.RelatoDiaRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;

public class RelatoDiaService {
    RelatoDiaRepository relatoDiaRepository;
    UsuarioRepository usuarioRepository;

    public RelatoDia criar(RelatoDia relato, Long usuarioId){

        if(relatoDiaRepository.existsByUsuarioIdAndDataRegistro(usuarioId, relato.getDataRegistro())){
            throw new RuntimeException("O usuário já fez o relato do dia " + relato.getDataRegistro());
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado")
                );

        relato.setUsuario(usuario);

        return relatoDiaRepository.save(relato);

    }

}
