package diario_emocional.ufrn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import diario_emocional.ufrn.enums.DiaSemana;
import diario_emocional.ufrn.entity.AtividadeObrigatoria;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AtividadeResponseDTO {
    private Long id;
    private String titulo;
    private String subtitulo;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFim;

    private List<DiaSemana> diasDaSemana;

    private LocalDateTime dataCriacao;
    private Boolean ativo;

    public AtividadeResponseDTO() {
    }

    public AtividadeResponseDTO(AtividadeObrigatoria entidade) {
        this.id = entidade.getId();
        this.titulo = entidade.getTitulo();
        this.subtitulo = entidade.getSubtitulo();
        this.horaInicio = entidade.getHoraInicio();
        this.horaFim = entidade.getHoraFim();
        this.diasDaSemana = entidade.getDiasDaSemana();
        this.dataCriacao = entidade.getDataCriacao();
        this.ativo = entidade.getAtivo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
