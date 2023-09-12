package net.guilhermejr.sistema.remedioservice.api.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SintomaResponse {

    private Long id;
    private String descricao;
    private Set<RemedioResumidoResponse> remedios;
    private LocalDateTime criado;
    private LocalDateTime atualizado;
    private UUID usuario;

}
