package diario_emocional.ufrn.dto;

import diario_emocional.ufrn.enums.DiaSemana;
import diario_emocional.ufrn.enums.TipoLembrete;

import java.time.LocalDate;
import java.time.LocalTime;

public class LembreteResponseDto {

    private Long id;
    private LocalTime hora;
    private LocalDate ultimaDataEnvio;
    private DiaSemana diaSemana;
    private TipoLembrete tipoLembrete;
    private String atividadeObrigatoriaTitulo;

    public LembreteResponseDto(
            Long id,
            LocalTime hora,
            LocalDate ultimaDataEnvio,
            DiaSemana diaSemana,
            TipoLembrete tipoLembrete,
            String atividadeObrigatoriaTitulo
    ) {
        this.id = id;
        this.hora = hora;
        this.ultimaDataEnvio = ultimaDataEnvio;
        this.diaSemana = diaSemana;
        this.tipoLembrete = tipoLembrete;
        this.atividadeObrigatoriaTitulo = atividadeObrigatoriaTitulo;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getHora() {
        return hora;
    }

    public LocalDate getUltimaDataEnvio() {
        return ultimaDataEnvio;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public TipoLembrete getTipoLembrete() {
        return tipoLembrete;
    }

    public String getAtividadeObrigatoriaTitulo() {
        return atividadeObrigatoriaTitulo;
    }
}