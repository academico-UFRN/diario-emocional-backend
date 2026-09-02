package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.RelatoDiaEditarDto;
import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.repository.RelatoDiaRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class RelatoDiaService {
    private final RelatoDiaRepository relatoDiaRepository;
    private final UsuarioRepository usuarioRepository;

    public RelatoDiaService(RelatoDiaRepository relatoDiaRepository, UsuarioRepository usuarioRepository) {
        this.relatoDiaRepository = relatoDiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public RelatoDia criar(RelatoDia relato, Long usuarioId) {

        // verifica se o relato vai ser criado em uma data futura
        if (relato.getDataRegistro().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O usuário não pode criar um relato em uma data futura."
            );
        }

        // verifica se usuario + relato já existem
        if (this.relatoDiaRepository.existsByUsuarioIdAndDataRegistro(usuarioId, relato.getDataRegistro())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O usuário já realizou o relato do dia, não podendo criar outro."
            );
        }

        // acha o usuario pelo id para relacionar ao relato
        Usuario usuario = this.usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "O usuário responsável não existe.")
                );

        relato.setUsuario(usuario);

        return this.relatoDiaRepository.save(relato);

    }


    public RelatoDia retornarRelatoEspecificoPorUsuario(LocalDate relatoId, Long usuarioId) {
        // verifica se o usuário existe
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "O usuário responsável não existe.")
                );

        // verifica se usuario + relato já existem
        if (!this.relatoDiaRepository.existsByUsuarioIdAndDataRegistro(usuarioId, relatoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi possível encontrar o relato para esse usuário nessa data."
            );
        } else {
            return this.relatoDiaRepository.findRelatoDiasByUsuarioIdAndDataRegistro(usuarioId, relatoId);
        }

    }

    public List<RelatoDia> retornarRelatosPorUsuario(Long usuarioId) {

        // verifica se o usuário existe
        Usuario usuario = this.usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "O usuário responsável não existe.")
                );

        return this.relatoDiaRepository.findAllByUsuarioId(usuarioId);

    }

    @Transactional
    public void deletar(LocalDate relatoId, Long usuarioId) {
        // verifica se o usuário existe
        Usuario usuario = this.usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "O usuário responsável não existe.")
                );

        // verifica se usuario + relato já existem
        if (!this.relatoDiaRepository.existsByUsuarioIdAndDataRegistro(usuarioId, relatoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi possível encontrar o relato para esse usuário nessa data."
            );
        }
        RelatoDia relato = this.relatoDiaRepository.findRelatoDiasByUsuarioIdAndDataRegistro(usuarioId, relatoId);

        this.relatoDiaRepository.delete(relato);

    }

    @Transactional
    public RelatoDia editar(RelatoDiaEditarDto relatoEditado, LocalDate relatoId, Long usuarioId) {
        // verifica se o usuário existe
        Usuario usuario = this.usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "O usuário responsável não existe.")
                );

        // verifica se usuario + relato já existem
        if (!this.relatoDiaRepository.existsByUsuarioIdAndDataRegistro(usuarioId, relatoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi possível encontrar o relato para esse usuário nessa data."
            );
        }

        RelatoDia relato = this.relatoDiaRepository.findRelatoDiasByUsuarioIdAndDataRegistro(usuarioId, relatoId);

        relato.From(relatoEditado);

        this.relatoDiaRepository.save(relato);;

        return relato;

    }
}
