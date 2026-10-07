package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.UsuarioRequestDTO;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
            throw new ResourceNotFoundException("Nome de usuário não foi encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Nome e senha não existem a um usuário válido.");
        }

        return usuario.getId();
    }

    public Usuario editarUsuario(String nome, String senha, Long id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não foi encontrado no banco de dados."));

        if (nome!=null) {
            usuario.setNome(nome);
        }

        if (senha !=null) {
            usuario.setSenha(senha);
        }

        usuarioRepository.save(usuario);

        return usuario;
    }

    public void deletarUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não foi encontrado no banco de dados."));

        usuarioRepository.delete(usuario);
    }
}
