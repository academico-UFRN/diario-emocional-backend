package diario_emocional.ufrn.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import diario_emocional.ufrn.dto.HistoryStreakResponse;
import org.springframework.stereotype.Service;

import diario_emocional.ufrn.entity.StreakPeriod;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.repository.StreakPeriodRepository;
import diario_emocional.ufrn.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class StreakPeriodService {
    private final StreakPeriodRepository streakPeriodRepository;
    private final UsuarioRepository usuarioService;

    public StreakPeriodService(StreakPeriodRepository streakPeriodRepository, UsuarioRepository usuarioService) {
        this.streakPeriodRepository = streakPeriodRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public void setStreakPeriod(Long userId) {
        Usuario usuario = usuarioService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId));

        Optional<StreakPeriod> streakPeriod = streakPeriodRepository.findFirstByUsuarioIdOrderByDataFinalDesc(userId);

        LocalDate today = LocalDate.now(ZoneId.of("America/Fortaleza"));

        if (streakPeriod.isPresent()) {
            if (streakPeriod.get().getDataFinal().equals(today)) {
                return; // Não faz nada se o usuário já completou o diário hoje
            }

            if (streakPeriod.get().getDataFinal().plusDays(1).equals(today)) {
                streakPeriod.get().incrementDay(LocalDate.now(ZoneId.of("America/Fortaleza")));
                streakPeriodRepository.save(streakPeriod.get());
                return;
            }
        }

        StreakPeriod newStreakPeriod = new StreakPeriod(usuario, today);
        streakPeriodRepository.save(newStreakPeriod);
    }

    public HistoryStreakResponse getHistoryStreakPeriod(Long userId) {
        Usuario usuario = usuarioService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId));

        StreakPeriod streakPeriod = streakPeriodRepository.findFirstByUsuarioIdOrderByDataFinalDesc(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Última streak não encontrada"));

        StreakPeriod higherStreakPeriod = streakPeriodRepository.findFirstByUsuarioIdOrderByQtdDiasDesc(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Maior streak não encontrada"));

        LocalDate tresMesesAtras = LocalDate.now().minusMonths(3);

        List<StreakPeriod> ultimos3meses = streakPeriodRepository.findByUsuarioIdAndDataFinalGreaterThanEqualOrderByDataInicioAsc(usuario.getId(), tresMesesAtras);

        List<LocalDate> dates = ultimos3meses.stream()
                .flatMap(period -> {
                    // Se a streak começou antes de 3 meses atrás, corta o início para não trazer dias de 4 meses atrás
                    LocalDate start = period.getDataInicio().isBefore(tresMesesAtras)
                            ? tresMesesAtras
                            : period.getDataInicio();

                    // datesUntil é exclusivo no final, então somamos +1 dia na dataFinal para incluí-la
                    return start.datesUntil(period.getDataFinal().plusDays(1));
                })
                .toList();

        return new HistoryStreakResponse(higherStreakPeriod, streakPeriod, dates);
    }
}
