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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

        validaRelatoDia(relato, usuarioId);

        // acha o usuario pelo id para relacionar ao relato
        Usuario usuario = validaUsuarioExistente(usuarioId);

        relato.setUsuario(usuario);

        return this.relatoDiaRepository.save(relato);

    }


    public RelatoDia retornarRelatoEspecificoPorUsuario(LocalDate relatoId, Long usuarioId) {
        // verifica se o usuário existe
        Usuario usuario = validaUsuarioExistente(usuarioId);

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

    public List<RelatoDia> retornarRelatosPorUsuario(Long usuarioId)  {

        // verifica se o usuário existe
        Usuario usuario = validaUsuarioExistente(usuarioId);

        return this.relatoDiaRepository.findAllByUsuarioId(usuarioId);

    }

    @Transactional
    public void deletar(LocalDate relatoId, Long usuarioId) {
        // verifica se o usuário existe
        Usuario usuario = validaUsuarioExistente(usuarioId);

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
    public RelatoDia editar(RelatoDiaEditarDto relatoEditado, LocalDate relatoId, Long usuarioId){
        // verifica se o usuário existe
        Usuario usuario = validaUsuarioExistente(usuarioId);

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

    private void validaRelatoDia(RelatoDia relatoDia, Long usuarioId) {
        List<String> erros = new ArrayList<>();

        if(this.relatoDiaRepository.existsByUsuarioIdAndDataRegistro(usuarioId, relatoDia.getDataRegistro())){
            erros.add("O usuário só pode ter um relato por dia");
        }

        if (relatoDia.getDataRegistro() == null) {
            erros.add("A data de registro é obrigatória.");
        }
        if(relatoDia.getDataRegistro().isAfter(LocalDate.now())) {
            erros.add("A data de registro não pode ser futura.");
        }

        if (relatoDia.getTitulo() == null || relatoDia.getTitulo().isBlank()) {
            erros.add("O título é obrigatório.");
        }

        if (relatoDia.getConteudoHtml() == null || relatoDia.getConteudoHtml().isBlank()) {
            erros.add("O conteúdo do relato é obrigatório.");
        }

        if (!erros.isEmpty()) {
            throw new IllegalArgumentException(String.join(" ", erros));
        }
    }

    private Usuario validaUsuarioExistente(Long usuarioId) {
        Usuario usuario = this.usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new NoSuchElementException("O usuário responsável não existe.")
                );
        return usuario;
    }
}
