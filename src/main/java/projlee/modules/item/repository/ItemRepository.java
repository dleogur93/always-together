package projlee.modules.item.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projlee.modules.item.domain.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.isDeleted = false AND LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Item> findByNameContainingIgnoreCase(Pageable pageable, @Param("name") String name);

    @Query("SELECT i FROM Item i WHERE i.isDeleted = false ORDER BY i.registrationDate DESC")
    List<Item> findTop12ByOrderByRegistrationDateDesc();

    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.id = :categoryId AND i.isDeleted = false")
    Page<Item> findByCategoryId(Pageable pageable, @Param("categoryId") Long categoryId);

    // 활성화된 아이템만 조회
    @Query("SELECT i FROM Item i WHERE i.isDeleted = false")
    Page<Item> findAllActive(Pageable pageable);

    // 특정 ID로 활성화된 아이템 조회
    @Query("SELECT i FROM Item i WHERE i.id = :id AND i.isDeleted = false")
    Optional<Item> findActiveById(@Param("id") Long id);

    // 삭제된 아이템 포함 모든 데이터 조회
    @Query("SELECT i FROM Item i")
    List<Item> findAllWithDeleted();



}
