package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.AtividadeRequestDTO;
import diario_emocional.ufrn.dto.AtividadeResponseDTO;
import diario_emocional.ufrn.dto.CronogramaResponseDTO;
import diario_emocional.ufrn.enums.DiaSemana;
import diario_emocional.ufrn.entity.AtividadeObrigatoria;
import diario_emocional.ufrn.repository.AtividadeObrigatoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service public class AtividadeObrigatoriaService {

    private final AtividadeObrigatoriaRepository repository;

    public AtividadeObrigatoriaService(AtividadeObrigatoriaRepository repository) {
        this.repository = repository;
    }


    // criar
    @Transactional public AtividadeResponseDTO criarAtividade(Long usuarioId, AtividadeRequestDTO dto) {
        validarHorarios(dto);
        validarConflitoDeHorarios(usuarioId, dto, null);

        AtividadeObrigatoria entidade = new AtividadeObrigatoria();
        entidade.setUsuarioId(usuarioId);
        entidade.setTitulo(dto.getTitulo());
        entidade.setSubtitulo(dto.getSubtitulo());
        entidade.setHoraInicio(dto.getHoraInicio());
        entidade.setHoraFim(dto.getHoraFim());
        entidade.setDiasDaSemana(dto.getDiasDaSemana());

        AtividadeObrigatoria salva = repository.save(entidade);
        return new AtividadeResponseDTO(salva);
    }


    // Listar cronograma
    @Transactional(readOnly = true)
    public CronogramaResponseDTO obterCronogramaDoUsuario(Long usuarioId) {
        List<AtividadeObrigatoria> atividades = repository.findByUsuarioIdAndAtivoTrue(usuarioId);

        List<AtividadeResponseDTO> dtos = atividades.stream()
                .map(AtividadeResponseDTO::new)
                .collect(Collectors.toList());

        return new CronogramaResponseDTO(
                1L,
                usuarioId,
                "Nome do Usuário",
                dtos
        );
    }

    // obter detalhes de uma atividade
    @Transactional(readOnly = true)
    public AtividadeResponseDTO obterPorIdEUsuario(Long usuarioId, Long id) {
        AtividadeObrigatoria atividade = repository.findByIdAndUsuarioIdAndAtivoTrue(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada ou não pertence ao usuário."));

        return new AtividadeResponseDTO(atividade);
    }

    // buscar atividade
    @Transactional(readOnly = true)
    public List<AtividadeResponseDTO> obterAtividadesPorDia(Long usuarioid, DiaSemana dia) {
        List<AtividadeObrigatoria> atividades = repository.findByUsuarioIdAndDiaSemana(usuarioid, dia);

        return atividades.stream()
                .map(AtividadeResponseDTO::new)
                .collect(Collectors.toList());
    }


    // atualizar
    @Transactional
    public AtividadeResponseDTO atualizarAtividade(Long usuarioId, Long id, AtividadeRequestDTO dto) {
        validarHorarios(dto);

        AtividadeObrigatoria atividade = repository.findByIdAndUsuarioIdAndAtivoTrue(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada para atualização."));

        validarConflitoDeHorarios(usuarioId, dto, id);

        atividade.setTitulo(dto.getTitulo());
        atividade.setSubtitulo(dto.getSubtitulo());
        atividade.setHoraInicio(dto.getHoraInicio());
        atividade.setHoraFim(dto.getHoraFim());
        atividade.setDiasDaSemana(dto.getDiasDaSemana());

        AtividadeObrigatoria atualizada = repository.save(atividade);
        return new AtividadeResponseDTO(atualizada);
    }

    // remover
    @Transactional
    public void deletarAtividade(Long usuarioid, Long id) {
        AtividadeObrigatoria atividade = repository.findByIdAndUsuarioIdAndAtivoTrue(id, usuarioid)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada para exclusão."));

        atividade.setAtivo(false);
        repository.save(atividade);
    }

    // validar
    private void validarHorarios(AtividadeRequestDTO dto) {
        if (dto.getHoraInicio() == null || dto.getHoraFim() == null) {
            throw new IllegalArgumentException("Os horários de início e término são obrigatórios.");
        }

        if (!dto.getHoraFim().isAfter(dto.getHoraInicio())) {
            throw new IllegalArgumentException("O horario de término deve ser posterior ao horário de início.");
        }
    }

    private void validarConflitoDeHorarios(Long usuarioId, AtividadeRequestDTO dto, Long atividadeIdExistente) {
        if (dto.getDiasDaSemana() == null || dto.getDiasDaSemana().isEmpty()) {
            return;
        }

        List<AtividadeObrigatoria> atividadesAtivas = repository.findByUsuarioIdAndAtivoTrue(usuarioId);

        for (AtividadeObrigatoria existente : atividadesAtivas) {
            if (atividadeIdExistente != null && existente.getId() != null && existente.getId().equals(atividadeIdExistente)) {
                continue;
            }

            if (existente.getDiasDaSemana() == null || existente.getDiasDaSemana().isEmpty()) {
                continue;
            }

            boolean temDiaEmComum = existente.getDiasDaSemana().stream()
                    .anyMatch(dia -> dto.getDiasDaSemana().contains(dia));

            if (temDiaEmComum) {
                boolean horarioSobrepoe = dto.getHoraInicio().isBefore(existente.getHoraFim())
                        && dto.getHoraFim().isAfter(existente.getHoraInicio());

                if (horarioSobrepoe) {
                    throw new IllegalArgumentException(
                            String.format("Conflito de horário com a atividade '%s' (%s - %s).",
                                    existente.getTitulo(), existente.getHoraInicio(), existente.getHoraFim())
                    );
                }
            }
        }
    }

}
