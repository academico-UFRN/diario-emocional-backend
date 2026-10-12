package diario_emocional.ufrn.entity;

import diario_emocional.ufrn.entity.Chat;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String senha; // guardar sempre o hash, nunca a senha pura

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RelatoDia> relatoDias = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RelatoHigieneSono> relatosHigieneSono = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AtividadeObrigatoria> atividadesObrigatorias = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvaliacaoSentimento> avaliacoesSentimento = new ArrayList<>();

    protected Usuario() { } // exigido pelo JPA

    public Usuario(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public List<Chat> getChats() { return chats; }
    public List<RelatoDia> getRelatoDias() { return relatoDias; }
    public List<RelatoHigieneSono> getRelatosHigieneSono() { return relatosHigieneSono; }
    public List<AtividadeObrigatoria> getAtividadesObrigatorias() { return atividadesObrigatorias; }
    public List<AvaliacaoSentimento> getAvaliacoesSentimento() { return avaliacoesSentimento; }
}