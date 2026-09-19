package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.LembreteDto;
import diario_emocional.ufrn.entity.AtividadeObrigatoria;
import diario_emocional.ufrn.entity.Lembrete;
import diario_emocional.ufrn.enums.DiaSemana;
import diario_emocional.ufrn.enums.TipoLembrete;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.repository.LembreteRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Calendar.*;

@Service
public class LembreteService {

    private final LembreteRepository lembreteRepository;

    LembreteService(LembreteRepository lembreteRepository){
        this.lembreteRepository = lembreteRepository;
    }


    public List<Lembrete> criar(
            LembreteDto lembretesDto,
            AtividadeObrigatoria atividadeObrigatoria
    ) {
        List<Lembrete> lembretes = new ArrayList<>();

        // [segunda, terça], [15_minutos_antes, no horario]
        // {segunda, 15_minutos_antes}, {terça, 15_minutos_antes}, {segunda_no horario}, {terça, no_horario}

        for (DiaSemana diaSemana : atividadeObrigatoria.getDiasDaSemana()) {

            for (TipoLembrete tipoLembrete :
                    lembretesDto.getTipoLembreteList()) {

                Lembrete lembrete = new Lembrete();

                lembrete.setAtividadeObrigatoria(atividadeObrigatoria);
                lembrete.setDiaSemana(diaSemana);
                LocalTime hora = atividadeObrigatoria.getHoraInicio();

                LocalTime horaLembrete = switch (tipoLembrete) {
                    case TRINTA_MINUTOS_ANTES -> hora.minusMinutes(30);
                    case QUINZE_MINUTOS_ANTES -> hora.minusMinutes(15);
                    case HORARIO -> hora;
                };

                lembrete.setHora(horaLembrete);

                lembreteRepository.save(lembrete);

                lembretes.add(lembrete);
            }
        }

        return lembretes;
    }
    public boolean horarioDoLembreteChegou(Lembrete lembrete) {

        LocalTime agora = LocalTime.now().withSecond(0).withNano(0);

        return agora.equals(lembrete.getHora());
    }
    public Lembrete verificarLembreteParaEnviar() {

        List<Lembrete> lembretes = lembreteRepository.findAll();

        LocalDate hoje = LocalDate.now();

        for (Lembrete lembrete : lembretes) {

            boolean aindaNaoEnviado =
                    lembrete.getUltimaDataEnvio() == null
                            || !lembrete.getUltimaDataEnvio().equals(hoje);

            if (aindaNaoEnviado && horarioDoLembreteChegou(lembrete)) {

                lembrete.setUltimaDataEnvio(hoje);
                lembreteRepository.save(lembrete);

                return lembrete;
            }
        }

        return null;
    }
    private DiaSemana converterDiaSemana(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> DiaSemana.SEGUNDA;
            case TUESDAY -> DiaSemana.TERCA;
            case WEDNESDAY -> DiaSemana.QUARTA;
            case THURSDAY -> DiaSemana.QUINTA;
            case FRIDAY -> DiaSemana.SEXTA;
            case SATURDAY -> DiaSemana.SABADO;
            case SUNDAY -> DiaSemana.DOMINGO;
        };
    }
}
