package projlee.modules.item.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
public class FoodForm {


    private String name;

    private int price;

    private int stockQuantity;

    private String itemPicture;

    private String itemInformation;

    private LocalDate expirationDate;

    private List<Long> categoryIds;





}

