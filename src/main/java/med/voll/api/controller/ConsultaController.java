package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import med.voll.api.consulta.CancelarConsulta;
import med.voll.api.consulta.Consulta;
import med.voll.api.consulta.ConsultaRepository;
import med.voll.api.consulta.DadosCadastroConsulta;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import med.voll.api.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import med.voll.api.services.ConsultaService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    @Autowired
    private ConsultaRepository repository;

    @Autowired
    private ConsultaService consultaService;

//    @Autowired
//    private MedicoRepository medicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody DadosCadastroConsulta dados) {

        List<String> falhas = consultaService.validarConsulta(dados);

        try {
            if (falhas.isEmpty()) {
                Consulta consulta = new Consulta(dados);
                repository.save(consulta);
                return ResponseEntity.status(HttpStatus.CREATED).body(consulta);
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body(falhas);
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a solicitação");
        }
    }


    @GetMapping
    public List<Consulta> listar() {
        return repository.findAllByAtivoTrue();
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> cancelar(@RequestBody @Valid CancelarConsulta cancelar) {
        boolean validConsulta = repository.findAllByIdAtivoTrue(cancelar.getConsulta());
        var consulta = repository.getReferenceById(cancelar.getConsulta());
        if (validConsulta) {
            if (consulta.antecedencia(consulta.getDataHora())) {
                consulta.cancelar(cancelar.getMotivo().toString());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Consulta cancelada com sucesso");
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("É necessário uma antecedência de 24 horas");
            }
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Consulta ja cancelada ou inexistente");
        }
    }


}
