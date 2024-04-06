package med.voll.api.consulta;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record DadosCadastroConsulta(
        Long medico,
        @NotNull

        Long paciente,

        @NotNull
        LocalDateTime dataHora) {

        @Override
        public Long medico() {
                return medico;
        }

        @Override
        public Long paciente() {
                return paciente;
        }

        @Override
        public LocalDateTime dataHora() {
                return dataHora;
        }
}
