package diario_emocional.ufrn.dto;

import java.util.List;

public class CronogramaResponseDTO {
    
    private Long cronogramaId;
    private Long usuarioId;
    private String usuarioNome;
    private List<AtividadeResponseDTO> atividades;

    public CronogramaResponseDTO() {
    }

    public CronogramaResponseDTO(Long cronogramaId, Long usuarioId, String usuarioNome, List<AtividadeResponseDTO> atividades) {
        this.cronogramaId = cronogramaId;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.atividades = atividades;
    }

    public Long getCronogramaId() {
        return cronogramaId;
    }

    public void setCronogramaId(Long cronogramaId) {
        this.cronogramaId = cronogramaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public List<AtividadeResponseDTO> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<AtividadeResponseDTO> atividades) {
        this.atividades = atividades;
    }

}
