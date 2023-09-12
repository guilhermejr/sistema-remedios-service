package net.guilhermejr.sistema.remedioservice.domain.repository;

import net.guilhermejr.sistema.remedioservice.domain.entity.Sintoma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SintomaRepository extends JpaRepository<Sintoma, Long> {

    Optional<Sintoma> findByDescricaoAndUsuario(String descricao, UUID usuario);
    List<Sintoma> findAllByUsuarioOrderByDescricaoAsc(UUID usuario);

}
