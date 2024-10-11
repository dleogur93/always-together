package projlee.modules.item.form;

import lombok.Data;
import projlee.modules.item.domain.Food;
import projlee.modules.item.domain.Item;
import projlee.modules.item.domain.Category;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemModifyForm {

    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private String itemPicture;

    private String itemInformation;

    private LocalDate expirationDate;

    private List<Long> categoryIds;

    @ConstructorProperties({"id"})
    public ItemModifyForm(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stockQuantity = item.getStockQuantity();
        this.itemPicture = item.getItemPicture();
        this.itemInformation = item.getItemInformation();
        if (item instanceof Food) {
            this.expirationDate = ((Food) item).getExpirationDate();
        }
        this.categoryIds = item.getCategories().stream().map(Category::getId).collect(Collectors.toList());

    }
}
