package net.guilhermejr.sistema.remedioservice.domain.repository;

import net.guilhermejr.sistema.remedioservice.domain.entity.Consumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumoRepository extends JpaRepository<Consumo, Long> {
}
