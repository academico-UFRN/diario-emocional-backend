package diario_emocional.ufrn.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "relato-do-dia")
public class RelatoDia {

    @Id
    private LocalDate dataRegistro;

    private String titulo;

    private String conteudoHtml;

    private boolean isFavorito;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    //efeito cascata, alteração em relato = alteração em avaliacao
    //remove orfãos que ficaram soltos de relato
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "avaliacao_sentimento_id", unique = true)
    private AvaliacaoSentimento avaliacaoSentimento;


    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getConteudoHtml() {
        return conteudoHtml;
    }

    public void setConteudoHtml(String conteudoHtml) {
        this.conteudoHtml = conteudoHtml;
    }


    public boolean isFavorito() {
        return isFavorito;
    }

    public void setFavorito(boolean favorito) {
        isFavorito = favorito;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public AvaliacaoSentimento getAvaliacaoSentimento() {
        return avaliacaoSentimento;
    }

    public void setAvaliacaoSentimento(
            AvaliacaoSentimento avaliacaoSentimento) {
        this.avaliacaoSentimento = avaliacaoSentimento;
    }

}
