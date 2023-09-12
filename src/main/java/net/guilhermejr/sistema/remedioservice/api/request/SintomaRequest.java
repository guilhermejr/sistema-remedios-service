package net.guilhermejr.sistema.remedioservice.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SintomaRequest {

    @NotBlank
    private String descricao;

}
