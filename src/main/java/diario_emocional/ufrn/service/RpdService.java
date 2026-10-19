package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.RpdRequestDTO;
import diario_emocional.ufrn.dto.RpdResponseDTO;
import diario_emocional.ufrn.entity.RegistroPensamentoDisfuncional;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.mapper.RpdMapper;
import diario_emocional.ufrn.repository.RpdRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RpdService {
    private final RpdRepository rpdRepository;
    private final UsuarioRepository usuarioRepository;
    private final RpdMapper mapper;

    public RpdService(RpdRepository rpdRepository, UsuarioRepository usuarioRepository, RpdMapper mapper) {
        this.rpdRepository = rpdRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Transactional
    public RpdResponseDTO criar(Long usuarioId, RpdRequestDTO dto) {
        Usuario usuario = buscarUsuario(usuarioId);
        RegistroPensamentoDisfuncional registro = mapper.toEntity(dto);
        registro.setUsuario(usuario);
        return mapper.toDTO(rpdRepository.save(registro));
    }

    public List<RpdResponseDTO> listar(Long usuarioId) {
        buscarUsuario(usuarioId);
        return rpdRepository.findAllByUsuario_IdOrderByDataHoraRegistroDesc(usuarioId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public RpdResponseDTO buscar(Long usuarioId, Long id) {
        buscarUsuario(usuarioId);
        return mapper.toDTO(buscarRegistro(usuarioId, id));
    }

    @Transactional
    public RpdResponseDTO editar(Long usuarioId, Long id, RpdRequestDTO dto) {
        buscarUsuario(usuarioId);
        RegistroPensamentoDisfuncional registro = buscarRegistro(usuarioId, id);
        mapper.atualizar(registro, dto);
        return mapper.toDTO(rpdRepository.save(registro));
    }

    @Transactional
    public void deletar(Long usuarioId, Long id) {
        buscarUsuario(usuarioId);
        rpdRepository.delete(buscarRegistro(usuarioId, id));
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o ID: " + usuarioId
                ));
    }

    private RegistroPensamentoDisfuncional buscarRegistro(Long usuarioId, Long id) {
        return rpdRepository.findByIdAndUsuario_Id(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de RPD não encontrado."));
    }
}
