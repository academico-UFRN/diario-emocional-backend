package diario_emocional.ufrn.service;

import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.repository.UsuarioRepository;

import java.util.Optional;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Long criarUsuario(String nome, String senha){
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNome(nome);

        if (usuarioOpt.isPresent()) {
            throw new IllegalArgumentException("O usuário já existe.");
        }

        Usuario novoUsuario = new Usuario(nome, senha);
        usuarioRepository.save(novoUsuario);

        return novoUsuario.getId();
    }

    public Long login(String nome, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNome(nome);

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("O usuário não foi encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("A senha não condiz com o usuário.");
        }

        return usuario.getId();
    }
}
