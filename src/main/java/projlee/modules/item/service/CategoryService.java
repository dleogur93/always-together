package projlee.modules.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.item.domain.Category;
import projlee.modules.item.form.CategoryForm;
import projlee.modules.item.repository.CategoryRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public void categoryRegistration(CategoryForm form) {


        Category newCategory = new Category();
        newCategory.setName(form.getName());

        if (form.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(form.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent category ID"));
            parentCategory.addChildCategory(newCategory);
        }

        categoryRepository.save(newCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid category ID"));

        category.removeCategory(); // 부모-자식 관계 해제 및 자식 카테고리 삭제 처리
        categoryRepository.delete(category); // 데이터베이스에서 삭제
    }

    public Category findById(Long categoryId) {
      return  categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + categoryId));
    }
}
