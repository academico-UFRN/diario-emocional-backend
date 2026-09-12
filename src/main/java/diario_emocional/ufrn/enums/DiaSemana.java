package diario_emocional.ufrn.enums;

public enum DiaSemana {
    SEGUNDA(1, "Segunda-Feira"),
    TERCA(2, "Terça-Feira"),
    QUARTA(3, "Quarta-Feira"),
    QUINTA(4, "Quinta-Feira"),
    SEXTA(5, "Sexta-Feira"),
    SABADO(6, "Sábado"),
    DOMINGO(0, "Domingo");

    private final int codigo;
    private final String descricao;

    DiaSemana(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
