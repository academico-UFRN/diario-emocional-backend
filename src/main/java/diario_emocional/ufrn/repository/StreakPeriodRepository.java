package diario_emocional.ufrn.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import diario_emocional.ufrn.entity.StreakPeriod;

public interface StreakPeriodRepository extends JpaRepository<StreakPeriod, UUID> {
    Optional<StreakPeriod> findFirstByUsuarioIdOrderByDataFinalDesc(Long userId);
    Optional<StreakPeriod> findFirstByUsuarioIdOrderByQtdDiasDesc(Long userId);
    List<StreakPeriod> findByUsuarioIdAndDataFinalGreaterThanEqualOrderByDataInicioAsc(Long userId, LocalDate startDate);
}
