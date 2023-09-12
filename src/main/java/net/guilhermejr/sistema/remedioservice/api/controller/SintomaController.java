package net.guilhermejr.sistema.remedioservice.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.remedioservice.api.request.SintomaRequest;
import net.guilhermejr.sistema.remedioservice.api.response.SintomaResponse;
import net.guilhermejr.sistema.remedioservice.service.SintomaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyRole('REMEDIOS')")
@RequestMapping("/sintomas")
public class SintomaController {

    private final SintomaService sintomaService;

    @GetMapping
    public ResponseEntity<List<SintomaResponse>> retornar() {

        log.info("Retornando sintomas");
        List<SintomaResponse> sintomaResponseList = sintomaService.retornar();
        return ResponseEntity.status(HttpStatus.OK).body(sintomaResponseList);

    }

    @GetMapping("/{id}")
    public ResponseEntity<SintomaResponse> retornarUm(@PathVariable Long id) {

        log.info("Recuperando um sintoma: {}", id);
        SintomaResponse sintomaResponse = sintomaService.retornarUm(id);
        return ResponseEntity.status(HttpStatus.OK).body(sintomaResponse);

    }

    @PostMapping
    public ResponseEntity<SintomaResponse> inserir(@Valid @RequestBody SintomaRequest sintomaRequest) {

        log.info("Inserindo sintoma");
        SintomaResponse sintomaResponse = sintomaService.inserir(sintomaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(sintomaResponse);

    }

    @PutMapping("/{id}")
    public ResponseEntity<SintomaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody SintomaRequest sintomaRequest) {

        log.info("Atualizando sintoma: {}", id);
        SintomaResponse sintomaResponse = sintomaService.atualizar(id, sintomaRequest);
        return ResponseEntity.status(HttpStatus.OK).body(sintomaResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {

        log.info("Apagando sintoma: {}", id);
        sintomaService.apagar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
