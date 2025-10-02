package digital.rj.apileadsandemails.Leads.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Foco {
    POSGRADUCAO,
    TECNICOS,
    PARCERIAS,
    IMPRENSA,
    OUTROS;

    @JsonCreator
    public static Foco from(Object raw) {
        if (raw == null) return OUTROS;
        String s = raw.toString();

        // normaliza: maiúsculas, remove espaços/hífens e caracteres acentuados comuns
        s = s.trim()
                .toUpperCase()
                .replace("Á","A").replace("Â","A").replace("Ã","A")
                .replace("É","E").replace("Ê","E")
                .replace("Í","I")
                .replace("Ó","O").replace("Ô","O")
                .replace("Ú","U")
                .replace("Ç","C")
                .replace("-", "")
                .replace(" ", "");

        switch (s) {
            case "POSGRADUACAO": return POSGRADUCAO;
            case "TECNICOS":     return TECNICOS;
            case "PARCERIAS":    return PARCERIAS;
            case "IMPRENSA":     return IMPRENSA;
            case "OUTROS":       return OUTROS;
            default:             return OUTROS;
        }
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}