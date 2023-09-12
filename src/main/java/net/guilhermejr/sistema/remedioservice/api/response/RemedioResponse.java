package net.guilhermejr.sistema.remedioservice.api.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RemedioResponse {

    private Long id;
    private String nome;
    private Integer quantidade;
    private String descricao;
    private String posologia;
    private Set<SintomaResumidoResponse > sintomas;
    private LocalDate validade;
    private Long diasParaValidade;
    private LocalDateTime criado;
    private LocalDateTime atualizado;
    private UUID usuario;

    public Long getDiasParaValidade() {

        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, this.validade);

    }

}
