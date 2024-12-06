package projlee.modules.item.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import projlee.exception.NotEnoughStockException;
import projlee.modules.item.form.ItemModifyForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Item {

    @Id @GeneratedValue
    @Column( name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @Lob @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "LONGTEXT")
    private String itemPicture;

    @Lob @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "LONGTEXT")
    private String itemInformation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime registrationDate;

    @ManyToMany(mappedBy = "items")
    @Builder.Default
    private List<Category> categories = new ArrayList<>();


    /**
     *
     * soft 삭제
     */

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 소프트 삭제 여부


    // 소프트 삭제
    public void softDelete() {
        this.isDeleted = true;
    }

    // 복원
    public void restore() {
        this.isDeleted = false;
    }

    // 삭제 여부 확인
    public boolean isDeleted() {
        return this.isDeleted;
    }


    public void addStockQuantity(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStockQuantity(int quantity) {

        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            throw new NotEnoughStockException("재고가 필요합니다.");
        }
        this.stockQuantity = restStock;

    }

    public void updateDetails(ItemModifyForm itemModifyForm) {
        this.name = itemModifyForm.getName();
        this.price = itemModifyForm.getPrice();
        this.stockQuantity = itemModifyForm.getStockQuantity();
        this.itemPicture = itemModifyForm.getItemPicture();
        this.itemInformation = itemModifyForm.getItemInformation();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category newCategory) {
        if (!categories.contains(newCategory)) {
            categories.add(newCategory);
            newCategory.getItems().add(this);
        }
    }

    public void removeCategory(Category categoryToRemove) {
        if (categories.contains(categoryToRemove)) {
            categories.remove(categoryToRemove);
            categoryToRemove.getItems().remove(this);
        }
    }
}
