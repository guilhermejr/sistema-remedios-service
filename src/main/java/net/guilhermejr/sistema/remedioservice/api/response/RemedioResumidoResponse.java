package net.guilhermejr.sistema.remedioservice.api.response;

import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RemedioResumidoResponse {

    private Long id;
    private String nome;
    private Integer quantidade;
    private String descricao;
    private String posologia;
    private LocalDate validade;
    private Long diasParaValidade;

    public Long getDiasParaValidade() {

        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, this.validade);

    }

}
