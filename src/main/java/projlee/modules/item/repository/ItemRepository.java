package projlee.modules.item.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projlee.modules.item.domain.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByNameContainingIgnoreCase(Pageable pageable, String name);

    List<Item> findTop12ByOrderByRegistrationDateDesc();

    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.id = :categoryId")
    Page<Item> findByCategoryId(Pageable pageable ,@Param("categoryId") Long categoryId);
}
