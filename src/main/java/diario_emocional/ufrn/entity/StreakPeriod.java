package diario_emocional.ufrn.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "streak_periods")
public class StreakPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate dataInicio;

    // Dia final da streak, que é atualizado a cada dia que o usuário completa
    @Column(nullable = false)
    private LocalDate dataFinal;

    @Column(nullable = false)
    private Integer qtdDias = 1;

    public StreakPeriod() {
    }

    public StreakPeriod(Usuario usuario, LocalDate today) {
        this.usuario = usuario;
        this.dataInicio = today;
        this.dataFinal = today;
        this.qtdDias = 1;
    }

    public void incrementDay(LocalDate today) {
        this.dataFinal = today;
        this.qtdDias += 1;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public Integer getQtdDias() {
        return qtdDias;
    }

    public UUID getId() {
        return id;
    }
}
