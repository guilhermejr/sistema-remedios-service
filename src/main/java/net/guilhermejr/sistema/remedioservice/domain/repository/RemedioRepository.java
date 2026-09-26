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

    /** Busca por id restringindo ao dono: id de outro usuário não é encontrado. */
    Optional<Remedio> findByIdAndUsuario(Long id, UUID usuario);

    /**
     * Remédios que vencem até a data informada, incluindo os que já venceram — um
     * remédio vencido ontem é mais urgente que um que vence em 29 dias, e uma faixa
     * a partir de hoje o deixaria de fora.
     */
    @Query("SELECT r FROM Remedio r WHERE r.usuario = :usuario AND r.validade <= :ate ORDER BY r.validade ASC")
    List<Remedio> findVencendoAte(@Param("usuario") UUID usuario, @Param("ate") LocalDate ate);

    @Query("SELECT r FROM Remedio r WHERE r.quantidade <= r.estoqueBaixo AND r.usuario = :usuario")
    List<Remedio> findRemediosComEstoqueBaixo(@Param("usuario") UUID usuario);

}
