package med.voll.api.services;

import med.voll.api.consulta.Consulta;
import med.voll.api.consulta.ConsultaRepository;
import med.voll.api.consulta.DadosCadastroConsulta;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import med.voll.api.paciente.PacienteRepository;
import org.hibernate.validator.constraintvalidators.RegexpURLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return dayOfWeek  != DayOfWeek.SUNDAY && horaConsulta >= 7 && horaConsulta <=19;
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

    public boolean isConsultaAbertaMedico(LocalDateTime dataHora, Long medico){
        boolean teste = consultaRepository.findMedicoDisponivel(medico, dataHora).isPresent();
        return teste;
    }


    public List<String> validarConsulta(DadosCadastroConsulta dados) {

        List<String> falhas = new ArrayList<>();
        List<Long> idMedico = new ArrayList<>();

        boolean clinicaAberta = isClinicaAberta(dados.getDataHora());
        if (!isClinicaAberta(dados.getDataHora())) {
            falhas.add("Clínica fechada no horário da consulta");
        }
        boolean medicoDisponivel = isMedicoDisponivel(dados.getMedico());
        if (!isMedicoDisponivel(dados.getMedico())) {
            falhas.add("Médico não está disponível");
        }
        boolean pacienteAtivo = isPacienteAtivo(dados.getPaciente());
        if (!isPacienteAtivo(dados.getPaciente())) {
            falhas.add("Paciente não está ativo ou não existe");
        }
        boolean consultaAberta = isConsultaAberta(dados.getDataHora(), dados.getPaciente());
        if (isConsultaAberta(dados.getDataHora(), dados.getPaciente())) {
            falhas.add("O paciente já possui uma consulta para esse dia");
        }

        boolean consultaAbertaMedico = isConsultaAbertaMedico(dados.getDataHora(), dados.getMedico());
        if (isConsultaAbertaMedico(dados.getDataHora(), dados.getMedico())) {
            falhas.add("O médico não está disponivel neste horário");
        }


        return falhas;
    }
}
