package projlee.modules.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projlee.modules.item.domain.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
