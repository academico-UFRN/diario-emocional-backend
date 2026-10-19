package diario_emocional.ufrn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registro_pensamento_disfuncional")
public class RegistroPensamentoDisfuncional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora_registro", nullable = false)
    private LocalDateTime dataHoraRegistro = LocalDateTime.now();

    @Column(name = "intensidade_crise", nullable = false)
    private Integer intensidadeCrise;

    @ElementCollection
    @CollectionTable(
            name = "rpd_emocoes_principais",
            joinColumns = @JoinColumn(name = "rpd_id")
    )
    @Column(name = "emocao", nullable = false)
    private List<String> emocoesPrincipais = new ArrayList<>();

    @Column(name = "gatilho_situacao", columnDefinition = "text")
    private String gatilhoSituacao;

    @Column(name = "pensamento_automatico", nullable = false, columnDefinition = "text")
    private String pensamentoAutomatico;

    @ElementCollection
    @CollectionTable(
            name = "rpd_distorcoes_cognitivas",
            joinColumns = @JoinColumn(name = "rpd_id")
    )
    @Column(name = "distorcao", nullable = false)
    private List<String> distorcoesCognitivas = new ArrayList<>();

    @Column(name = "evidencias_contra_argumento", columnDefinition = "text")
    private String evidenciasContraArgumento;

    @Column(name = "pensamento_alternativo", columnDefinition = "text")
    private String pensamentoAlternativo;

    @Column(name = "intensidade_reavaliada")
    private Integer intensidadeReavaliada;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    public RegistroPensamentoDisfuncional() {
    }

    @PrePersist
    private void preencherDataHoraRegistro() {
        if (dataHoraRegistro == null) {
            dataHoraRegistro = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public Integer getIntensidadeCrise() {
        return intensidadeCrise;
    }

    public void setIntensidadeCrise(Integer intensidadeCrise) {
        this.intensidadeCrise = intensidadeCrise;
    }

    public List<String> getEmocoesPrincipais() {
        return emocoesPrincipais;
    }

    public void setEmocoesPrincipais(List<String> emocoesPrincipais) {
        this.emocoesPrincipais = emocoesPrincipais == null
                ? new ArrayList<>()
                : new ArrayList<>(emocoesPrincipais);
    }

    public String getGatilhoSituacao() {
        return gatilhoSituacao;
    }

    public void setGatilhoSituacao(String gatilhoSituacao) {
        this.gatilhoSituacao = gatilhoSituacao;
    }

    public String getPensamentoAutomatico() {
        return pensamentoAutomatico;
    }

    public void setPensamentoAutomatico(String pensamentoAutomatico) {
        this.pensamentoAutomatico = pensamentoAutomatico;
    }

    public List<String> getDistorcoesCognitivas() {
        return distorcoesCognitivas;
    }

    public void setDistorcoesCognitivas(List<String> distorcoesCognitivas) {
        this.distorcoesCognitivas = distorcoesCognitivas == null
                ? new ArrayList<>()
                : new ArrayList<>(distorcoesCognitivas);
    }

    public String getEvidenciasContraArgumento() {
        return evidenciasContraArgumento;
    }

    public void setEvidenciasContraArgumento(String evidenciasContraArgumento) {
        this.evidenciasContraArgumento = evidenciasContraArgumento;
    }

    public String getPensamentoAlternativo() {
        return pensamentoAlternativo;
    }

    public void setPensamentoAlternativo(String pensamentoAlternativo) {
        this.pensamentoAlternativo = pensamentoAlternativo;
    }

    public Integer getIntensidadeReavaliada() {
        return intensidadeReavaliada;
    }

    public void setIntensidadeReavaliada(Integer intensidadeReavaliada) {
        this.intensidadeReavaliada = intensidadeReavaliada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
