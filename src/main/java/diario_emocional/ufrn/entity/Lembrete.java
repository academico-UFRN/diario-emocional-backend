package diario_emocional.ufrn.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import diario_emocional.ufrn.enums.DiaSemana;
import diario_emocional.ufrn.enums.TipoLembrete;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Lembrete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime hora;
    private LocalDate ultimaDataEnvio;

    @Column(name = "dia_semana")
    private DiaSemana diaSemana;

    @Column(name = "tipo_lembrete")
    private TipoLembrete tipoLembrete;

    @ManyToOne
    @JoinColumn(name = "atividade_obrigatoria_id", nullable = false)
    @JsonIgnore
    private AtividadeObrigatoria atividadeObrigatoria;

    public void setUltimaDataEnvio(LocalDate ultimaDataEnvio) {
        this.ultimaDataEnvio = ultimaDataEnvio;
    }

    public void setId(Long id){this.id = id;}

    public void setDiaSemana (DiaSemana diaSemana){this.diaSemana = diaSemana;}

    public void setHora(LocalTime hora){this.hora = hora;}

    public void setTipoLembrete(TipoLembrete tipoLembrete){this.tipoLembrete = tipoLembrete;};

    public void setAtividadeObrigatoria(AtividadeObrigatoria atividadeObrigatoria){this.atividadeObrigatoria = atividadeObrigatoria;}

    public Long getId() {
        return this.id;
    }


    public LocalTime getHora() {
        return this.hora;
    }

    public LocalDate getUltimaDataEnvio() {
        return this.ultimaDataEnvio;
    }

    public DiaSemana getDiaSemana() {
        return this.diaSemana;
    }

    public TipoLembrete getTipoLembrete() {
        return this.tipoLembrete;
    }

    public AtividadeObrigatoria getAtividadeObrigatoria() {
        return this.atividadeObrigatoria;
    }
}
