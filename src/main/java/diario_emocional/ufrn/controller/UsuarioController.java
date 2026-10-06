package diario_emocional.ufrn.controller;

import diario_emocional.ufrn.dto.LembreteResponseDto;
import diario_emocional.ufrn.dto.UsuarioRequestDTO;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Long> criarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO){

        Long usuarioCriadoId = this.usuarioService.criarUsuario(usuarioRequestDTO.getNome(), usuarioRequestDTO.getSenha());

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriadoId);

    }


    @GetMapping("/login")
    public ResponseEntity<Long> loginUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO){

        Long usuarioCriadoId = this.usuarioService.login(usuarioRequestDTO.getNome(), usuarioRequestDTO.getSenha());

        return ResponseEntity.status(HttpStatus.OK).body(usuarioCriadoId);

    }



    @PutMapping("/editar/{id}")
    public ResponseEntity<Usuario> editarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO usuarioRequestDTO){

        Usuario usuarioEditado = this.usuarioService.editarUsuario(usuarioRequestDTO.getNome(), usuarioRequestDTO.getSenha(), id);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioEditado);

    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Usuario> deletarUsuario(@PathVariable Long id){

        this.usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
