package projlee.modules.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;
import projlee.modules.banner.BannerService;
import projlee.modules.item.domain.Category;
import projlee.modules.item.domain.Item;
import projlee.modules.item.form.CategoryForm;
import projlee.modules.item.service.CategoryService;
import projlee.modules.item.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ItemService itemService;
    private final BannerService bannerService;

    @GetMapping("/manager/categoryRegistration")
    public String categoryRegistrationForm(@CurrentAccount Account account, Model model) {
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("categoryForm", new CategoryForm());
        model.addAttribute("account", account);
        return "manager/categoryRegistrationForm";
    }

    @PostMapping("/manager/categoryRegistration")
    public String categoryRegistration(@CurrentAccount Account account,@Valid CategoryForm categoryForm, BindingResult result, RedirectAttributes attributes,Model model) {

        if (result.hasErrors()) {
            return "manager/categoryRegistrationForm";
        }
        model.addAttribute("account", account);
        categoryService.categoryRegistration(categoryForm);
        attributes.addFlashAttribute("message", "카테고리가 등록되었습니다.");

        return "redirect:/manager/categoryRegistration";
    }


    @GetMapping("/manager/categoryDelete")
    public String categoryDeleteForm(@CurrentAccount Account account, Model model) {

        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("account",account);
        return "manager/categoryDelete";
    }

    @PostMapping("/manager/categoryDelete")
    public String categoryDelete(@RequestParam("id") Long id, RedirectAttributes attributes) {

        categoryService.deleteCategory(id);
        attributes.addFlashAttribute("message","삭제 완료");

        return "redirect:/manager/categoryDelete";
    }



    @GetMapping("/category/{categoryId}")
    public String showCategoryItems(
            @CurrentAccount Account account,
            @PageableDefault(size = 9, sort = "registrationDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "itemName", required = false) String itemName,
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "sort", defaultValue = "registrationDate") String sortProperty,
            @RequestParam(value = "direction", defaultValue = "desc") String direction,
            Model model) {

        if (account == null) {
            return "index";
        }

        // 카테고리 조회
        Category category = categoryService.findById(categoryId);
        List<Category> categories = categoryService.findAllCategories();

        // 페이지 설정
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortDirection, sortProperty));

        // 카테고리에 속하는 상품 목록 조회
        Page<Item> items = getItemPage(pageable, itemName, categoryId);

        // 모델에 데이터 추가
        model.addAttribute("categories", categories);
        model.addAttribute("category", category);
        model.addAttribute("items", items);
        model.addAttribute("sortProperty", sortProperty);
        model.addAttribute("direction", direction);
        model.addAttribute("banner",bannerService.findOne());

        return "item/itemCategoryList"; // Thymeleaf 템플릿 이름
    }

    private Page<Item> getItemPage( Pageable pageable, String itemName, Long categoryId) {
        if (itemName != null && !itemName.isEmpty()) {
            return itemService.searchItemsByItemName(pageable, itemName);
        } else {
            return itemService.findByCategoryId(pageable, categoryId);
        }
    }

}
