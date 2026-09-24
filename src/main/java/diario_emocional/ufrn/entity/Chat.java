package diario_emocional.ufrn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "modelo")
    private String modelo = "gemini-3.5-flash-lite";

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Mensagem> mensagems = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Construtor padrão exigido pelo JPA
    public Chat() {
    }

    public Chat(String titulo, Usuario usuario) {
        validar(titulo);

        this.titulo = titulo;
        this.usuario = usuario;
    }

    private void validar(String titulo) {
        if(titulo.isBlank()) {
            throw new IllegalArgumentException("Título está vazio.");
        }
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public List<Mensagem> getMensagems() {
        return mensagems;
    }

    public void setMensagems(List<Mensagem> mensagems) {
        this.mensagems = mensagems;
    }

    // Métodos utilitários de associação
    public void addMessage(Mensagem message) {
        mensagems.add(message);
        message.setChat(this);
    }

    public void removeMessage(Mensagem message) {
        mensagems.remove(message);
        message.setChat(null);
    }
}
