package net.guilhermejr.sistema.remedioservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "remedios")
public class Remedio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "remedios_sintomas",
        joinColumns = @JoinColumn(name = "remedio_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "sintoma_id", referencedColumnName = "id"))
    @JsonManagedReference
    private Set<Sintoma> sintomas;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Integer dose;

    @Column(nullable = false)
    private Integer estoqueBaixo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String posologia;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contraIndicacao;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate validade;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @CreationTimestamp
    private LocalDateTime criado;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @UpdateTimestamp
    private LocalDateTime atualizado;

    @Column(nullable = false)
    private UUID usuario;

}
