package org.example.portalbillion_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Entity
@Table(name = "clients")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "A razão social é obrigatoria!")
    @Column(name = "legal_name")
    private String legalName;

    @NotBlank(message = "O CNPJ é obrigatorio")
    @Pattern(
            regexp = "[0-9]{14}",
            message = "O cnpj deve conter 14 digitos numericos, sem pontuacao"
    )
    @Column(name = "cnpj", nullable = false, unique = true, length = 14)
    private String cnpj;

    @NotBlank(message = "O regime tributario é obrigatorio")
    @Column(name = "tax_regime")
    private String taxRegime;

    @NotNull(message = "O status é obrigatorio")
    private Boolean status;

    public String getTaxRegime() {
        return taxRegime;
    }

    public void setTaxRegime(String taxRegime) {
        this.taxRegime = taxRegime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
