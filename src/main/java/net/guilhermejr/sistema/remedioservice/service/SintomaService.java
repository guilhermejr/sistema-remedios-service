package net.guilhermejr.sistema.remedioservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.remedioservice.api.mapper.SintomaMapper;
import net.guilhermejr.sistema.remedioservice.api.request.SintomaRequest;
import net.guilhermejr.sistema.remedioservice.api.response.SintomaResponse;
import net.guilhermejr.sistema.remedioservice.config.security.AuthenticationCurrentUserService;
import net.guilhermejr.sistema.remedioservice.domain.entity.Sintoma;
import net.guilhermejr.sistema.remedioservice.domain.repository.SintomaRepository;
import net.guilhermejr.sistema.remedioservice.exception.ExceptionDefault;
import net.guilhermejr.sistema.remedioservice.exception.ExceptionNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class SintomaService {

    private final SintomaRepository sintomaRepository;
    private final SintomaMapper sintomaMapper;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    public List<SintomaResponse> retornar() {

        UUID usuario = authenticationCurrentUserService.getCurrentUser().getId();

        List<Sintoma> sintomaList = sintomaRepository.findAllByUsuarioOrderByDescricaoAsc(usuario);
        return sintomaMapper.mapList(sintomaList);

    }

    public SintomaResponse retornarUm(Long id) {

        Sintoma sintoma = sintomaExiste(id);
        if (sintoma != null) {
            return sintomaMapper.mapObject(sintoma);
        } else {
            log.error("Sintoma não retornado: {}", id);
            throw new ExceptionNotFound("Não pode retornar sintoma. Id não encontrado: " + id);
        }

    }

    @Transactional
    public SintomaResponse inserir(SintomaRequest sintomaRequest) {

        UUID usuario = authenticationCurrentUserService.getCurrentUser().getId();

        verificaSeExisteSintomaCadastrado(sintomaRequest.getDescricao(), usuario);

        LocalDateTime data = LocalDateTime.now(ZoneId.of("UTC"));

        Sintoma sintoma = sintomaMapper.mapObject(sintomaRequest);
        sintoma.setCriado(data);
        sintoma.setAtualizado(data);
        sintoma.setUsuario(usuario);

        Sintoma sintomaSave = sintomaRepository.save(sintoma);
        return sintomaMapper.mapObject(sintomaSave);

    }

    @Transactional
    public SintomaResponse atualizar(Long id, SintomaRequest sintomaRequest) {

        Sintoma sintoma = sintomaExiste(id);
        if (sintoma != null) {

            UUID usuario = authenticationCurrentUserService.getCurrentUser().getId();

            verificaSeExisteSintomaCadastrado(sintomaRequest.getDescricao(), usuario);

            LocalDateTime data = LocalDateTime.now(ZoneId.of("UTC"));

            sintoma.setDescricao(sintomaRequest.getDescricao());
            sintoma.setAtualizado(data);

            Sintoma sintomaSave = sintomaRepository.save(sintoma);
            return sintomaMapper.mapObject(sintomaSave);

        } else {
            log.error("Sintoma não atualizado: {}", id);
            throw new ExceptionNotFound("Não pode atualizar sintoma. Id não encontrado: " + id);
        }

    }

    @Transactional
    public void apagar(Long id) {

        Sintoma sintoma = sintomaExiste(id);

        if (sintoma != null) {

            sintomaRepository.deleteById(id);

        } else {
            log.error("Sintoma não apagado: {}", id);
            throw new ExceptionNotFound("Não pode apagar sintoma. Id não encontrado: " + id);
        }

    }

    private void verificaSeExisteSintomaCadastrado(String descricao, UUID usuario) {

        if (sintomaRepository.findByDescricaoAndUsuario(descricao, usuario).isPresent()) {
            log.error("Sintoma já cadastrado {}", descricao);
            throw new ExceptionDefault("Sintoma já cadastrado");
        }

    }

    private Sintoma sintomaExiste(Long id) {
        Optional<Sintoma> sintoma = sintomaRepository.findById(id);
        return sintoma.orElse(null);
    }

}
