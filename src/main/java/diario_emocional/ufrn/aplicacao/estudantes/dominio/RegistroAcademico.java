package diario_emocional.ufrn.aplicacao.estudantes.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import diario_emocional.ufrn.entity.Usuario;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "registro_academico")
public class RegistroAcademico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoExperienciaAcademica tipo;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @Column(name = "data_referencia", nullable = false)
    private LocalDate dataReferencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoExperienciaAcademica situacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    public RegistroAcademico() {
    }

    public Long getId() {
        return id;
    }

    public TipoExperienciaAcademica getTipo() {
        return tipo;
    }

    public void setTipo(TipoExperienciaAcademica tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataReferencia() {
        return dataReferencia;
    }

    public void setDataReferencia(LocalDate dataReferencia) {
        this.dataReferencia = dataReferencia;
    }

    public SituacaoExperienciaAcademica getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoExperienciaAcademica situacao) {
        this.situacao = situacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
