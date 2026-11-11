package diario_emocional.ufrn.aplicacao.estudantes.service;

import diario_emocional.ufrn.aplicacao.estudantes.dominio.RegistroAcademico;
import diario_emocional.ufrn.aplicacao.estudantes.dto.RegistroAcademicoRequest;
import diario_emocional.ufrn.aplicacao.estudantes.repository.RegistroAcademicoRepository;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroAcademicoService {
    private final RegistroAcademicoRepository registroRepository;
    private final UsuarioRepository usuarioRepository;

    public RegistroAcademicoService(
            RegistroAcademicoRepository registroRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.registroRepository = registroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public RegistroAcademico criar(Long usuarioId, RegistroAcademicoRequest request) {
        Usuario usuario = buscarUsuario(usuarioId);
        RegistroAcademico registro = new RegistroAcademico();
        aplicar(registro, request);
        registro.setUsuario(usuario);
        return registroRepository.save(registro);
    }

    @Transactional(readOnly = true)
    public List<RegistroAcademico> listar(Long usuarioId) {
        buscarUsuario(usuarioId);
        return registroRepository.findAllByUsuario_IdOrderByDataReferenciaDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public RegistroAcademico buscar(Long usuarioId, Long registroId) {
        buscarUsuario(usuarioId);
        return buscarRegistro(usuarioId, registroId);
    }

    @Transactional
    public RegistroAcademico atualizar(
            Long usuarioId,
            Long registroId,
            RegistroAcademicoRequest request
    ) {
        buscarUsuario(usuarioId);
        RegistroAcademico registro = buscarRegistro(usuarioId, registroId);
        aplicar(registro, request);
        return registroRepository.save(registro);
    }

    @Transactional
    public void deletar(Long usuarioId, Long registroId) {
        buscarUsuario(usuarioId);
        registroRepository.delete(buscarRegistro(usuarioId, registroId));
    }

    private void aplicar(RegistroAcademico registro, RegistroAcademicoRequest request) {
        registro.setTipo(request.tipo());
        registro.setTitulo(request.titulo().trim());
        registro.setDescricao(request.descricao());
        registro.setDataReferencia(request.dataReferencia());
        registro.setSituacao(request.situacao());
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o ID: " + usuarioId));
    }

    private RegistroAcademico buscarRegistro(Long usuarioId, Long registroId) {
        return registroRepository.findByIdAndUsuario_Id(registroId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Registro acadêmico não encontrado para esse usuário."));
    }
}
