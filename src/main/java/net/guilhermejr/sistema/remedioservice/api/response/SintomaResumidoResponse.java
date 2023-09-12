package net.guilhermejr.sistema.remedioservice.api.response;

import lombok.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SintomaResumidoResponse {

    private Long id;
    private String descricao;

}
