package diario_emocional.ufrn.dto;

import diario_emocional.ufrn.entity.StreakPeriod;

import java.time.LocalDate;
import java.util.List;

public record HistoryStreakResponse(
        StreakPeriod MaiorStreak,
        StreakPeriod UltimaStreak,
        List<LocalDate> datas
) {
}
