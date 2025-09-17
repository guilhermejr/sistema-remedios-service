package net.guilhermejr.sistema.remedioservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.remedioservice.api.mapper.RemedioMapper;
import net.guilhermejr.sistema.remedioservice.api.request.RemedioRequest;
import net.guilhermejr.sistema.remedioservice.api.response.RemedioResponse;
import net.guilhermejr.sistema.remedioservice.config.security.AuthenticationCurrentUserService;
import net.guilhermejr.sistema.remedioservice.domain.entity.Consumo;
import net.guilhermejr.sistema.remedioservice.domain.entity.Remedio;
import net.guilhermejr.sistema.remedioservice.domain.entity.Sintoma;
import net.guilhermejr.sistema.remedioservice.domain.repository.ConsumoRepository;
import net.guilhermejr.sistema.remedioservice.domain.repository.RemedioRepository;
import net.guilhermejr.sistema.remedioservice.domain.repository.SintomaRepository;
import net.guilhermejr.sistema.remedioservice.exception.ExceptionDefault;
import net.guilhermejr.sistema.remedioservice.exception.ExceptionNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class RemedioService {

    private final RemedioRepository remedioRepository;
    private final SintomaRepository sintomaRepository;
    private final ConsumoRepository consumoRepository;
    private final RemedioMapper remedioMapper;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    public List<RemedioResponse> retornar() {

        UUID usuario = authenticationCurrentUserService.getCurrentUser().getId();

        List<Remedio> remedioList = remedioRepository.findAllByUsuarioOrderByNomeAsc(usuario);
        return remedioMapper.mapList(remedioList);

    }

    public List<RemedioResponse> retornarEstoqueBaixo() {

        UUID usuario = authenticationCurrentUserService.getCurrentUser().getId();

        List<Remedio> remedioList = remedioRepository.findRemediosComEstoqueBaixo(usuario);
        return remedioMapper.mapList(remedioList);

    }

    public RemedioResponse retornarUm(Long id) {

        Remedio remedio = remedioExiste(id);
        if (remedio != null) {
            return remedioMapper.mapObject(remedio);
        } else {
            log.error("Remédio não retornado: {}", id);
            throw new ExceptionNotFound("Não pode retornar remédio. Id não encontrado: " + id);
        }

    }

    @Transactional
    public RemedioResponse inserir(RemedioRequest remedioRequest) {

        return salvar(remedioRequest, null);

    }

    @Transactional
    public RemedioResponse atualizar(Long id, RemedioRequest remedioRequest) {

        if (remedioExiste(id) != null) {
            return salvar(remedioRequest, id);
        } else {
            log.error("Remédio não atualizado: {}", id);
            throw new ExceptionNotFound("Não pode atualizar remédio. Id não encontrado: " + id);
        }

    }

    @Transactional
    public RemedioResponse consumir(Long id) {

        Remedio remedio = remedioExiste(id);

        if (remedio != null) {

            Integer quantidade = remedio.getQuantidade();
            Integer dose = remedio.getDose();

            if (quantidade > 0 && quantidade >= dose) {

                remedio.setQuantidade(remedio.getQuantidade() - dose);

                UUID usuario = authenticationCurrentUserService.getCurrentUser().getId();

                Consumo consumo = Consumo
                        .builder()
                        .remedio(remedio)
                        .usuario(usuario)
                        .build();

                consumoRepository.save(consumo);
                return remedioMapper.mapObject(remedio);

            } else {
                log.error("Quantidade de remédios menor ou igual a 0: {}", id);
                throw new ExceptionNotFound("Não pode consumir remédio. Quantidade menor ou igual a 0: " + id);
            }


        } else {
            log.error("Remédio não consumido: {}", id);
            throw new ExceptionNotFound("Não pode consumir remédio. Id não encontrado: " + id);
        }

    }

    @Transactional
    public void apagar(Long id) {

        Remedio remedio = remedioExiste(id);

        if (remedio != null) {

            remedioRepository.deleteById(id);

        } else {
            log.error("Remédio não apagado: {}", id);
            throw new ExceptionNotFound("Não pode apagar remédio. Id não encontrado: " + id);
        }

    }

    public List<RemedioResponse> listarVencimento(Integer dias) {

        LocalDate hoje = LocalDate.now();
        LocalDate ate = hoje.plusDays(dias);
        return remedioMapper.mapList(remedioRepository.findByValidadeBetween(hoje, ate));

    }

    private RemedioResponse salvar(RemedioRequest remedioRequest, Long id) {

        UUID usuario = authenticationCurrentUserService.getCurrentUser().getId();

        if (id == null) {
            verificaSeExisteRemedioCadastrado(remedioRequest.getNome(), usuario);
        }

        LocalDateTime data = LocalDateTime.now(ZoneId.of("UTC"));

        Remedio remedio = remedioMapper.mapObject(remedioRequest);

        Set<Sintoma> sintomas = new HashSet<>();
        remedio.getSintomas().forEach(s -> {
            Sintoma sintoma = sintomaRepository.findById(s.getId())
                    .orElseThrow(() -> {
                        log.error("Sintoma {} não encontrado para o usuário {}", s.getId(), usuario);
                        return new ExceptionNotFound("Sintoma não encontrado");
                    });
            sintomas.add(sintoma);
        });

        if (id != null) {
            remedio.setId(id);
        }

        if (id == null) {
            remedio.setCriado(data);
        }

        remedio.setSintomas(sintomas);
        remedio.setAtualizado(data);
        remedio.setUsuario(usuario);

        Remedio remedioSave = remedioRepository.save(remedio);
        return remedioMapper.mapObject(remedioSave);

    }

    private void verificaSeExisteRemedioCadastrado(String nome, UUID usuario) {

        if (remedioRepository.findByNomeAndUsuario(nome, usuario).isPresent()) {
            log.error("Remédio já cadastrado {}", nome);
            throw new ExceptionDefault("Remédio já cadastrado");
        }

    }

    private Remedio remedioExiste(Long id) {
        Optional<Remedio> remedio = remedioRepository.findById(id);
        return remedio.orElse(null);
    }

}
