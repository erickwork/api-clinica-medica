package med.voll.api.services;

import med.voll.api.consulta.Consulta;
import med.voll.api.consulta.ConsultaRepository;
import med.voll.api.consulta.DadosCadastroConsulta;
import med.voll.api.medico.MedicoRepository;
import med.voll.api.paciente.PacienteRepository;
import org.hibernate.validator.constraintvalidators.RegexpURLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ConsultaService {

    @Autowired
    private  MedicoRepository medicoRepository;

    @Autowired
    private  ConsultaRepository consultaRepository;

    @Autowired
    private  PacienteRepository pacienteRepository;


    private static boolean isClinicaAberta(LocalDateTime dataHora){
        DayOfWeek dayOfWeek  = dataHora.getDayOfWeek();
        int horaConsulta = dataHora.getHour();
        return dayOfWeek  != dayOfWeek.MONDAY && horaConsulta >= 7 && horaConsulta <=19;
    }

    private  boolean isMedicoDisponivel(Long medico){
        boolean teste = medicoRepository.findById(medico).isPresent();
        return teste;
    }

    private boolean isPacienteAtivo(Long paciente){
        boolean teste = pacienteRepository.findById(paciente).isPresent();
        return teste;
    }

    private  boolean isConsultaAberta(LocalDateTime dataHora, Long paciente){
        boolean teste = consultaRepository.findPacienteDisponivel(paciente, dataHora).isPresent();
        return teste;
    }

    private boolean isConsultaAbertaMedico(LocalDateTime dataHora, Long medico){
        boolean teste = consultaRepository.findMedicoDisponivel(medico, dataHora).isPresent();
        return teste;
    }



    public boolean validarConsulta(DadosCadastroConsulta dados) {


        boolean clinicaAberta = isClinicaAberta(dados.dataHora());
        boolean medicoDisponivel = isMedicoDisponivel(dados.medico());
        boolean pacienteAtivo = isPacienteAtivo(dados.paciente());
        boolean consultaAberta = isConsultaAberta(dados.dataHora(), dados.paciente());
        boolean consultaAbertaMedico = isConsultaAbertaMedico(dados.dataHora(), dados.medico());
        if(clinicaAberta && medicoDisponivel && pacienteAtivo && !consultaAbertaMedico && !consultaAberta) return true;
        else{
            return false;
         }
    }
}
