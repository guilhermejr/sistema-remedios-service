package net.guilhermejr.sistema.remedioservice.api.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RemedioResponse extends RemedioResumidoResponse {


    private String descricao;
    private String posologia;
    private String contraIndicacao;
    private Set<SintomaResumidoResponse > sintomas;
    private LocalDateTime criado;
    private LocalDateTime atualizado;
    private UUID usuario;


}
