package med.voll.api.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    @Query("SELECT m FROM Medico m WHERE m.ativo = true")
    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Medico m WHERE m.id = :medico AND m.ativo = true")
    boolean findAByIdAtivoTrue(Long medico);

//    @Query("SELECT m FROM Medico m WHERE m.ativo = true")
//    List<Medico> findAllByAtivoTrues();

//    @Query("SELECT m FROM Medico m WHERE m.id = :medico and m.ativo = true ")
//    boolean findAllByAtivoTrue(Long medico);

}
