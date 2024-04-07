package med.voll.api.consulta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.medico.Medico;
import med.voll.api.paciente.Paciente;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "consulta")
@Entity(name = "Consulta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Consulta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medico")
    private Long medico;

    @Column(name = "paciente")
    private Long paciente;

    @Column(name = "ativo")
    private boolean ativo;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    public Consulta(DadosCadastroConsulta dados) {
        this.ativo = true;
        this.paciente = dados.getPaciente();
        this.medico = dados.getMedico();
        this.dataHora = dados.getDataHora();

    }

    public void excluir() {
        this.ativo = false;
    }
}
