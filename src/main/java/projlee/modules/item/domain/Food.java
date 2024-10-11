package projlee.modules.item.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import projlee.modules.item.form.ItemModifyForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@DiscriminatorValue("F")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Food extends Item{

    private LocalDate expirationDate;

    @Override
    public void updateDetails(ItemModifyForm itemModifyForm) {
        super.updateDetails(itemModifyForm);
        this.expirationDate = itemModifyForm.getExpirationDate();
    }



}
