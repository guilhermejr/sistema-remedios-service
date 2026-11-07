package net.guilhermejr.sistema.remedioservice.domain.repository;

import net.guilhermejr.sistema.remedioservice.domain.entity.Consumo;
import net.guilhermejr.sistema.remedioservice.domain.entity.Remedio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumoRepository extends JpaRepository<Consumo, Long> {

    /** A FK de consumos para remedios não tem cascade: apagar o remédio exige apagar estes antes. */
    @Modifying
    @Query("DELETE FROM Consumo c WHERE c.remedio = :remedio")
    void apagarDoRemedio(@Param("remedio") Remedio remedio);

}
