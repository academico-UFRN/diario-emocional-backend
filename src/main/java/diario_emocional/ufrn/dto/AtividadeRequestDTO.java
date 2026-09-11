package diario_emocional.ufrn.dto;

import diario_emocional.ufrn.enums.DiaSemana;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

public class AtividadeRequestDTO {
    
    @NotBlank(message = "O título da atividade é obrigatório.")
    private String titulo;

    private String subtitulo;

    @NotNull(message = "O horário de inicio é obrigatório.")
    private LocalTime horaInicio;

    @NotNull(message = "O horário de término é obrigatório.")
    private LocalTime horaFim;

    @NotEmpty(message = "Informe ao menos um dia da semana para a atividade.")
    private List<DiaSemana> diasDaSemana;

    public AtividadeRequestDTO() {
    }

    public AtividadeRequestDTO(String titulo, String subtitulo, LocalTime horaInicio, LocalTime horaFim, List<DiaSemana> diasDaSemana) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.diasDaSemana = diasDaSemana;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public List<DiaSemana> getDiasDaSemana() {
        return diasDaSemana;
    }

    public void setDiasDaSemana(List<DiaSemana> diasDaSemana) {
        this.diasDaSemana = diasDaSemana;
    }
}
