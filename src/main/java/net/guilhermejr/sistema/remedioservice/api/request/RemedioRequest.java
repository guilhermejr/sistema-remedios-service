package net.guilhermejr.sistema.remedioservice.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.guilhermejr.sistema.remedioservice.api.request.validation.constraints.DataBrasil;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RemedioRequest {

    @NotBlank
    private String nome;

    @Min(0)
    @NotNull
    private Integer quantidade;

    @Min(1)
    @NotNull
    private Integer dose;

    @Min(1)
    @NotNull
    private Integer estoqueBaixo;

    @NotBlank
    private String descricao;

    @NotBlank
    private String posologia;

    @NotBlank
    private String contraIndicacao;

    @NotBlank
    @DataBrasil
    private String validade;

    @NotEmpty
    private Set<Long> sintomas;

}
