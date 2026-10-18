package diario_emocional.ufrn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import diario_emocional.ufrn.enums.NivelDisposicao;
import diario_emocional.ufrn.enums.QualidadeSono;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "relato_higiene_sono",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_relato_sono_usuario_data",
                columnNames = {"usuario_id", "data_registro"}
        )
)
public class RelatoHigieneSono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @Column(name = "hora_dormir", nullable = false)
    private LocalTime horaDormir;

    @Column(name = "hora_acordar", nullable = false)
    private LocalTime horaAcordar;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualidade_sono", nullable = false, length = 20)
    private QualidadeSono qualidadeSono;

    @Column(name = "usou_celular", nullable = false)
    private boolean usouCelular;

    @Column(name = "teve_pesadelos", nullable = false)
    private boolean tevePesadelos;

    @Column(name = "comentario_sonhos", length = 1000)
    private String comentarioSonhos;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_disposicao", nullable = false, length = 20)
    private NivelDisposicao nivelDisposicao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    public RelatoHigieneSono() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public LocalTime getHoraDormir() {
        return horaDormir;
    }

    public void setHoraDormir(LocalTime horaDormir) {
        this.horaDormir = horaDormir;
    }

    public LocalTime getHoraAcordar() {
        return horaAcordar;
    }

    public void setHoraAcordar(LocalTime horaAcordar) {
        this.horaAcordar = horaAcordar;
    }

    public QualidadeSono getQualidadeSono() {
        return qualidadeSono;
    }

    public void setQualidadeSono(QualidadeSono qualidadeSono) {
        this.qualidadeSono = qualidadeSono;
    }

    public boolean isUsouCelular() {
        return usouCelular;
    }

    public void setUsouCelular(boolean usouCelular) {
        this.usouCelular = usouCelular;
    }

    public boolean isTevePesadelos() {
        return tevePesadelos;
    }

    public void setTevePesadelos(boolean tevePesadelos) {
        this.tevePesadelos = tevePesadelos;
    }

    public String getComentarioSonhos() {
        return comentarioSonhos;
    }

    public void setComentarioSonhos(String comentarioSonhos) {
        this.comentarioSonhos = comentarioSonhos;
    }

    public NivelDisposicao getNivelDisposicao() {
        return nivelDisposicao;
    }

    public void setNivelDisposicao(NivelDisposicao nivelDisposicao) {
        this.nivelDisposicao = nivelDisposicao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public long getDuracaoSonoMinutos() {
        Duration duracao = Duration.between(horaDormir, horaAcordar);
        if (duracao.isNegative()) {
            duracao = duracao.plusHours(24);
        }
        return duracao.toMinutes();
    }
}