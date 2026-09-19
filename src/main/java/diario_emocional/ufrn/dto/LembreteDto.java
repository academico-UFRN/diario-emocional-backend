package diario_emocional.ufrn.dto;

import diario_emocional.ufrn.enums.DiaSemana;
import diario_emocional.ufrn.enums.TipoLembrete;

import java.time.LocalTime;
import java.util.List;

public class LembreteDto {

    private Long id;

    private List<TipoLembrete> tipoLembreteList;

    public Long getId() {
        return this.id;
    }


    public List<TipoLembrete> getTipoLembreteList() {
        return this.tipoLembreteList;
    }
}