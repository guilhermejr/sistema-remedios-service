package net.guilhermejr.sistema.remedioservice.domain.repository;

import net.guilhermejr.sistema.remedioservice.domain.entity.Remedio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RemedioRepository extends JpaRepository<Remedio, Long> {

    Optional<Remedio> findByNomeAndUsuario(String nome, UUID usuario);

    List<Remedio> findAllByUsuarioOrderByNomeAsc(UUID usuario);

    List<Remedio> findByValidadeBetween(LocalDate hoje, LocalDate ate);

    @Query("SELECT r FROM Remedio r WHERE r.quantidade <= r.estoqueBaixo AND r.usuario = :usuario")
    List<Remedio> findRemediosComEstoqueBaixo(@Param("usuario") UUID usuario);

}
