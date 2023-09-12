package net.guilhermejr.sistema.remedioservice.domain.repository;

import net.guilhermejr.sistema.remedioservice.domain.entity.Remedio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RemedioRepository extends JpaRepository<Remedio, Long> {

    Optional<Remedio> findByNomeAndUsuario(String nome, UUID usuario);

    List<Remedio> findAllByUsuarioOrderByNomeAsc(UUID usuario);
}
