package net.guilhermejr.sistema.remedioservice.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.guilhermejr.sistema.remedioservice.api.request.RemedioRequest;
import net.guilhermejr.sistema.remedioservice.api.response.RemedioResponse;
import net.guilhermejr.sistema.remedioservice.service.RemedioService;
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
@RequestMapping("/remedios")
public class RemedioController {

    private final RemedioService remedioService;

    @GetMapping
    public ResponseEntity<List<RemedioResponse>> retornar() {

        log.info("Retornando remédios");
        List<RemedioResponse> remedioResponseList = remedioService.retornar();
        return ResponseEntity.status(HttpStatus.OK).body(remedioResponseList);

    }

    @GetMapping("/{id}")
    public ResponseEntity<RemedioResponse> retornarUm(@PathVariable Long id) {

        log.info("Recuperando um remédio: {}", id);
        RemedioResponse remedioResponse = remedioService.retornarUm(id);
        return ResponseEntity.status(HttpStatus.OK).body(remedioResponse);

    }

    @PostMapping
    public ResponseEntity<RemedioResponse> inserir(@Valid @RequestBody RemedioRequest remedioRequest) {

        log.info("Inserindo remédio");
        RemedioResponse remedioResponse = remedioService.inserir(remedioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(remedioResponse);

    }

    @PutMapping("/{id}")
    public ResponseEntity<RemedioResponse> atualizar(@PathVariable Long id, @Valid @RequestBody RemedioRequest remedioRequest) {

        log.info("Atualizando remédio: {}", id);
        RemedioResponse remedioResponse = remedioService.atualizar(id, remedioRequest);
        return ResponseEntity.status(HttpStatus.OK).body(remedioResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {

        log.info("Apagando remédio: {}", id);
        remedioService.apagar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
