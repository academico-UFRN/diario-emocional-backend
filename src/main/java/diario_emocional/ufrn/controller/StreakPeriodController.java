package diario_emocional.ufrn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diario_emocional.ufrn.dto.HistoryStreakResponse;
import diario_emocional.ufrn.service.StreakPeriodService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/streak-period")
public class StreakPeriodController {
    private final StreakPeriodService streakPeriodService;

    public StreakPeriodController(StreakPeriodService streakPeriodService) {
        this.streakPeriodService = streakPeriodService;
    }

    @GetMapping("/{userId}")
    public HistoryStreakResponse getHistoryStreak(@PathVariable Long userId) {
        return streakPeriodService.getHistoryStreakPeriod(userId);
    }

}
