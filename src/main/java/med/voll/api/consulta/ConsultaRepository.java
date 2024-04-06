package med.voll.api.consulta;

import med.voll.api.paciente.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    //@Query("SELECT c FROM Consulta c WHERE c.paciente = :paciente and c.dataHora = cast(:dataHora as localdatetime ) and c.ativo = true")
    //boolean findPacienteDisponivel(Long paciente, LocalDateTime dataHora);

    @Query("SELECT c FROM Consulta c WHERE c.paciente = :paciente and DATE(c.dataHora) = DATE(:dataHora) and c.ativo = true ")
    Optional<Consulta> findPacienteDisponivel(Long paciente, LocalDateTime dataHora);


    @Query("SELECT c FROM Consulta c WHERE c.medico = :medico and c.dataHora = :dataHora  and c.ativo = true ")
    Optional<Consulta>findMedicoDisponivel(Long medico, LocalDateTime dataHora);

}
