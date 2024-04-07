package med.voll.api.consulta;

import jakarta.validation.constraints.NotNull;
import lombok.Setter;

import java.time.LocalDateTime;




public class DadosCadastroConsulta{

        private Long medico;
        @NotNull
        private Long paciente;

        @NotNull
        private LocalDateTime dataHora;


        public Long getMedico() {
                return medico;
        }

        public void setMedico(Long medico) {
                this.medico = medico;
        }

        public Long getPaciente() {
                return paciente;
        }

        public void setPaciente(Long paciente) {
                this.paciente = paciente;
        }

        public LocalDateTime getDataHora() {
                return dataHora;
        }

        public void setDataHora(LocalDateTime dataHora) {
                this.dataHora = dataHora;
        }

        public DadosCadastroConsulta(Long medico, Long paciente, LocalDateTime dataHora){
                this.dataHora = dataHora;
                this.medico = medico;
                this.paciente = paciente;
        }


}
