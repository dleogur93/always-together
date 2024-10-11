package projlee.modules.item.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id" )
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
                joinColumns = @JoinColumn(name = "category_id"),
                inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

    public void addItem(Item item) {
        this.items.add(item);
        if (item.getCategories() == null) {
            item.setCategories(new ArrayList<>());
        }
        item.getCategories().add(this);
    }

    public void removeItem(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            item.getCategories().remove(this);
        }
    }

    public void removeCategory() {
        for (Item item : new ArrayList<>(items)) {
            removeItem(item);
        }
        if (parent != null) {
            parent.getChild().remove(this);
            parent = null;
        }
        for (Category childCategory : child) {
            childCategory.setParent(null);
        }
        child.clear();
    }

}
