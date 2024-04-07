package med.voll.api.consulta;


import med.voll.api.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {


    //@Query("SELECT c FROM Consulta c WHERE c.paciente = :paciente and c.dataHora = cast(:dataHora as localdatetime ) and c.ativo = true")
    //boolean findPacienteDisponivel(Long paciente, LocalDateTime dataHora);

    @Query("SELECT c FROM Consulta c WHERE c.paciente = :paciente and DATE(c.dataHora) = DATE(:dataHora) and c.ativo = true ")
    Optional<Consulta> findPacienteDisponivel(Long paciente, LocalDateTime dataHora);


    @Query("SELECT c FROM Consulta c WHERE c.medico = :medico and c.dataHora = :dataHora  and c.ativo = true ")
    Optional<Consulta>findMedicoDisponivel(Long medico, LocalDateTime dataHora);

    @Query("SELECT c.medico FROM Consulta c WHERE c.medico In (SELECT m.id FROM Medico m WHERE m.ativo = true) AND c.medico NOT IN (SELECT c.medico FROM Consulta c WHERE  c.dataHora = :dataHora and c.ativo = true)")
    List<Long> buscarMedicoParaConsulta(LocalDateTime dataHora);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Consulta c where c.id = :consulta and c.ativo = true")
    boolean findAllByIdAtivoTrue(Long consulta);



    List<Consulta> findAllByAtivoTrue();
}
