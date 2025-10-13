package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@DiscriminatorValue("INSTITUTION_FINANCIERE")
public class InstitutionFinanciere extends Utilisateur {
    public InstitutionFinanciere() {}
}
