package med.voll.api.consulta;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CancelarConsulta {

    @NotNull
    private Long consulta;

    @NotNull @Enumerated(EnumType.STRING)
    private MotivoCancelamento motivo;

    public CancelarConsulta(Long consulta, MotivoCancelamento motivo) {
        this.consulta = consulta;
        this.motivo = motivo;
    }
}
