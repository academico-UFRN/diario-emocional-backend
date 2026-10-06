package diario_emocional.ufrn.dto;

public class UsuarioRequestDTO {

    private String nome;
    private String senha;

    public String getNome(){
        return this.nome;
    }

    public String getSenha(){
        return this.senha;
    }

}
