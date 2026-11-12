package diario_emocional.ufrn.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "avaliacoes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Avaliacao {

    @Id
    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "avalicao_dia")
    private Integer avaliacaoDia;

    @Column(name = "texto_livre")
    private String textoLivre;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    protected Avaliacao() {}

    protected Avaliacao(Integer avaliacaoDia, String textoLivre) {
        this.avaliacaoDia = avaliacaoDia;
        this.textoLivre = textoLivre;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Integer getAvaliacaoDia() {
        return avaliacaoDia;
    }

    public void setAvaliacaoDia(Integer avaliacaoDia) {
        this.avaliacaoDia = avaliacaoDia;
    }

    public String getTextoLivre() {
        return textoLivre;
    }

    public void setTextoLivre(String textoLivre) {
        this.textoLivre = textoLivre;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
