package diario_emocional.ufrn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import diario_emocional.ufrn.enums.MensagemRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mensagens")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "papel")
    @Enumerated(EnumType.STRING)
    private MensagemRole papel;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
    private Chat chat;

    // Construtor padrão exigido pelo JPA
    public Mensagem() {
    }

    public Mensagem(MensagemRole papel, String conteudo, Chat chat) {
        this.papel = papel;
        this.conteudo = conteudo;
        this.chat = chat;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MensagemRole getPapel() {
        return papel;
    }

    public void setPapel(MensagemRole papel) {
        this.papel = papel;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}