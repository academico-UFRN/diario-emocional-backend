package diario_emocional.ufrn.framework.service;

import diario_emocional.ufrn.framework.entity.Avaliacao;
import diario_emocional.ufrn.framework.entity.Usuario;
import diario_emocional.ufrn.framework.exception.ResourceNotFoundException;
import diario_emocional.ufrn.framework.repository.AvaliacaoBaseRepository;
import diario_emocional.ufrn.framework.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

public abstract class AvaliacaoService<T extends Avaliacao, RequestDTO, ResponseDTO> {

    protected final UsuarioRepository usuarioRepository;
    protected final StreakPeriodService streakPeriodService;

    protected AvaliacaoService(UsuarioRepository usuarioRepository, StreakPeriodService streakPeriodService) {
        this.usuarioRepository = usuarioRepository;
        this.streakPeriodService = streakPeriodService;
    }

    protected abstract AvaliacaoBaseRepository<T> getRepository();

    protected abstract ResponseDTO convertToDTO(T entity);

    protected abstract T convertToEntity(RequestDTO dto);

    protected abstract T atualizarCampos(T existente, T dadosNovos);

    protected abstract RuntimeException notFoundException(LocalDate dataRegistro);

    protected abstract void validarEspecifico(T entity);

    protected void validarBase(T entity) {
        if (entity.getAvaliacaoDia() == null || entity.getAvaliacaoDia() < 1 || entity.getAvaliacaoDia() > 5) {
            throw new IllegalArgumentException("A avaliação do dia deve ser uma nota de 1 a 5.");
        }
        if (entity.getTextoLivre() != null && entity.getTextoLivre().length() > 250) {
            throw new IllegalArgumentException("O texto livre não pode ultrapassar 250 caracteres.");
        }
    }

    protected Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o ID: " + usuarioId));
    }

    @Transactional
    public ResponseDTO criar(Long usuarioId, RequestDTO dto) {
        LocalDate data = LocalDate.now();
        Usuario usuario = buscarUsuario(usuarioId);

        getRepository().findByUsuarioAndDataRegistro(usuario, data)
                .ifPresent(registro -> {
                    throw new IllegalArgumentException("Já possui um registro hoje!");
                });

        T entity = convertToEntity(dto);
        validarBase(entity);
        validarEspecifico(entity);

        entity.setDataRegistro(data);
        entity.setUsuario(usuario);

        streakPeriodService.setStreakPeriod(usuarioId);

        return convertToDTO(getRepository().save(entity));
    }

    public List<ResponseDTO> listar(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);

        return getRepository().findByUsuario(usuario).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ResponseDTO get(Long usuarioId, LocalDate dataRegistro) {
        Usuario usuario = buscarUsuario(usuarioId);

        T entity = getRepository().findByUsuarioAndDataRegistro(usuario, dataRegistro)
                .orElseThrow(() -> notFoundException(dataRegistro));

        return convertToDTO(entity);
    }

    @Transactional
    public ResponseDTO editar(RequestDTO dto, Long usuarioId, LocalDate dataRegistro) {
        Usuario usuario = buscarUsuario(usuarioId);

        T existente = getRepository().findByUsuarioAndDataRegistro(usuario, dataRegistro)
                .orElseThrow(() -> notFoundException(dataRegistro));

        T dadosNovos = convertToEntity(dto);
        validarBase(dadosNovos);
        validarEspecifico(dadosNovos);

        T atualizado = atualizarCampos(existente, dadosNovos);

        return convertToDTO(getRepository().save(atualizado));
    }

    @Transactional
    public void deletar(LocalDate dataRegistro, Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);

        T entity = getRepository().findByUsuarioAndDataRegistro(usuario, dataRegistro)
                .orElseThrow(() -> notFoundException(dataRegistro));

        getRepository().delete(entity);
    }
}
