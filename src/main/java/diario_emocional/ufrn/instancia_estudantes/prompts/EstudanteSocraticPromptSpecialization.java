package diario_emocional.ufrn.instancia_estudantes.prompts;

import diario_emocional.ufrn.framework.prompts.SocraticPromptSpecialization;
import org.springframework.stereotype.Component;

@Component
public class EstudanteSocraticPromptSpecialization implements SocraticPromptSpecialization {

    @Override
    public String orientacoesDeDominio() {
        return """
                Considere o contexto de estudantes universitários. Ao refletir sobre as avaliações
                fornecidas, explore possíveis relações entre emoções e experiências com estudos ou
                provas somente quando esses temas estiverem presentes nos registros. Não presuma que
                a pessoa tenha provas, dificuldades acadêmicas ou uma rotina específica. Trate qualquer
                padrão como uma possibilidade a ser explorada pela própria pessoa, não como diagnóstico,
                conclusão ou relação de causa e efeito.
                """;
    }
}
