package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.RelatoHigieneSonoRequestDTO;
import diario_emocional.ufrn.dto.RelatoHigieneSonoResponseDTO;
import diario_emocional.ufrn.entity.RelatoHigieneSono;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.exception.DuplicateResourceException;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.mapper.RelatoHigieneSonoMapper;
import diario_emocional.ufrn.repository.RelatoHigieneSonoRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RelatoHigieneSonoService {
    private final RelatoHigieneSonoRepository relatoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RelatoHigieneSonoMapper mapper;

    public RelatoHigieneSonoService(
            RelatoHigieneSonoRepository relatoRepository,
            UsuarioRepository usuarioRepository,
            RelatoHigieneSonoMapper mapper
    ) {
        this.relatoRepository = relatoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Transactional
    public RelatoHigieneSonoResponseDTO criar(Long usuarioId, RelatoHigieneSonoRequestDTO dto) {
        validar(dto);
        Usuario usuario = buscarUsuario(usuarioId);

        if (relatoRepository.existsByUsuario_IdAndDataRegistro(usuarioId, dto.dataRegistro())) {
            throw new DuplicateResourceException("Já existe um relato de sono para esse usuário nessa data.");
        }

        RelatoHigieneSono relato = mapper.toEntity(dto);
        relato.setUsuario(usuario);
        return mapper.toDTO(relatoRepository.save(relato));
    }

    public List<RelatoHigieneSonoResponseDTO> listar(Long usuarioId) {
        buscarUsuario(usuarioId);
        return relatoRepository.findAllByUsuario_IdOrderByDataRegistroDesc(usuarioId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public RelatoHigieneSonoResponseDTO buscar(Long usuarioId, Long id) {
        buscarUsuario(usuarioId);
        return mapper.toDTO(buscarRelato(usuarioId, id));
    }

    @Transactional
    public RelatoHigieneSonoResponseDTO editar(Long usuarioId, Long id, RelatoHigieneSonoRequestDTO dto) {
        validar(dto);
        buscarUsuario(usuarioId);
        RelatoHigieneSono relato = buscarRelato(usuarioId, id);

        if (!relato.getDataRegistro().equals(dto.dataRegistro())
                && relatoRepository.existsByUsuario_IdAndDataRegistro(usuarioId, dto.dataRegistro())) {
            throw new DuplicateResourceException("Já existe um relato de sono para esse usuário nessa data.");
        }

        mapper.atualizar(relato, dto);
        return mapper.toDTO(relatoRepository.save(relato));
    }

    @Transactional
    public void deletar(Long usuarioId, Long id) {
        buscarUsuario(usuarioId);
        relatoRepository.delete(buscarRelato(usuarioId, id));
    }

    private void validar(RelatoHigieneSonoRequestDTO dto) {
        if (dto.horaDormir().equals(dto.horaAcordar())) {
            throw new IllegalArgumentException("Os horários de dormir e acordar não podem ser iguais.");
        }
        if (dto.dataRegistro().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data do relato não pode ser futura.");
        }
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));
    }

    private RelatoHigieneSono buscarRelato(Long usuarioId, Long id) {
        return relatoRepository.findByIdAndUsuario_Id(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Relato de higiene do sono não encontrado."));
    }
}