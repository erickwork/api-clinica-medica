package med.voll.api.services;

import med.voll.api.consulta.Consulta;
import med.voll.api.consulta.ConsultaRepository;
import med.voll.api.consulta.DadosCadastroConsulta;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import med.voll.api.paciente.PacienteRepository;
import org.hibernate.validator.constraintvalidators.RegexpURLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
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
        return  medicoRepository.findAByIdAtivoTrue(medico);
    }

    private boolean isPacienteAtivo(Long paciente){
        return pacienteRepository.findAByIdAtivoTrue(paciente);
    }

    private  boolean isConsultaAberta(LocalDateTime dataHora, Long paciente){
        return consultaRepository.findPacienteDisponivel(paciente, dataHora).isPresent();
    }

    public boolean isConsultaAbertaMedico(LocalDateTime dataHora, Long medico){
        return consultaRepository.findMedicoDisponivel(medico, dataHora).isPresent();
    }

    private boolean isAntecedencia(LocalDateTime dataHora){
        LocalDateTime agora = LocalDateTime.now();
        Duration diferenca = Duration.between(agora, dataHora);
        return diferenca.toMinutes() >= 30;
    }


    public List<String> validarConsulta(DadosCadastroConsulta dados) {

        List<String> falhas = new ArrayList<>();

        if (!isAntecedencia(dados.getDataHora())){
            falhas.add("Antecedência menor que 30 minutos");
        }


        if (!isClinicaAberta(dados.getDataHora())) {
            falhas.add("Clínica fechada no horário da consulta");
        }

        if (!isPacienteAtivo(dados.getPaciente())) {
            falhas.add("Paciente não está ativo ou não existe");
        }else{
            if (isConsultaAberta(dados.getDataHora(), dados.getPaciente())) {
                falhas.add("O paciente já possui uma consulta para esse dia");
            }
        }

        if (dados.getMedico() == null){
            List<Long> consultas = consultaRepository.buscarMedicoParaConsulta(dados.getDataHora());
            if (consultas.isEmpty()){
                falhas.add("Sem médico disponivel para horário");
            }else{
                Long idMedico = consultas.getFirst();
                dados.setMedico(idMedico);
                if (!isMedicoDisponivel(dados.getMedico())) {
                    falhas.add("Médico não está disponível");
                }else{
                    if (isConsultaAbertaMedico(dados.getDataHora(), dados.getMedico())) {
                        falhas.add("O médico não está disponivel neste horário");
                    }
                }

            }
        }else{

            if (!isMedicoDisponivel(dados.getMedico())) {
                falhas.add("Médico não está disponível");
            }else{
                if (isConsultaAbertaMedico(dados.getDataHora(), dados.getMedico())) {
                    falhas.add("O médico não está disponivel neste horário");
                }
            }

        }
        return falhas;
    }
}
