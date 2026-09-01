package diario_emocional.ufrn.service;

import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.repository.RelatoDiaRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

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
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O usuário não pode criar um relato em uma data futura."
            );
        }

        // verifica se usuario + relato já existem
        if(relatoDiaRepository.existsByUsuarioIdAndDataRegistro(usuarioId, relato.getDataRegistro())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O usuário já realizou o relato do dia, não podendo criar outro."
            );
        }

        // acha o usuario pelo id para relacionar ao relato
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Não foi possível encontrar o relato para esse usuário nessa data.")
                );

        relato.setUsuario(usuario);

        return relatoDiaRepository.save(relato);

    }


    public RelatoDia retornarRelatoEspecificoPorUsuario(LocalDate relatoId, Long usuarioId){

        // verifica se usuario + relato já existem
        if(!relatoDiaRepository.existsByUsuarioIdAndDataRegistro(usuarioId, relatoId)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi possível encontrar o relato para esse usuário nessa data."
            );
        }else{
            return relatoDiaRepository.findRelatoDiasByUsuarioIdAndDataRegistro(usuarioId, relatoId);
        }

    }

    public List<RelatoDia> retornarRelatosPorUsuario(Long usuarioId){
        return relatoDiaRepository.findAllByUsuarioId(usuarioId);

    }


}
