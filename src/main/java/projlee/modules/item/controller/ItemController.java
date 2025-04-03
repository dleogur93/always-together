package projlee.modules.item.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;
import projlee.modules.banner.BannerService;
import projlee.modules.dog.service.DogService;
import projlee.modules.item.domain.Category;
import projlee.modules.item.domain.Food;
import projlee.modules.item.domain.Item;
import projlee.modules.item.service.CategoryService;
import projlee.modules.item.service.ItemService;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final DogService dogService;
    private final CategoryService categoryService;
    private final BannerService bannerService;

    @GetMapping("/item/description/{itemId}")
    public String item(@CurrentAccount Account account,@PathVariable("itemId") Long id , Model model) {

        if (account != null) {

            Item item = itemService.findById(id);
            if (item instanceof Food) {
                Food food = (Food) item;
                model.addAttribute("item", food);
            } else {
                model.addAttribute("item", item);
            }


            model.addAttribute("account", account);

            return "item/description";
        }

        return "index";
    }

    @GetMapping("/item/mainList")
    public String itemMainList(@CurrentAccount Account account ,
                               @PageableDefault(size = 18, sort = "registrationDate", direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam(name = "itemName", required = false) String itemName,
                               @RequestParam(value = "direction", defaultValue = "desc") String direction,
                                Model model) {

        if (account == null) {
            model.addAttribute("itemList", itemService.itemFirstPage());
            model.addAttribute("dogList", dogService.firstPage());
            return "index";
        }

        model.addAttribute("account", account);
        List<Category> categories = categoryService.findAllCategories();
        Page<Item> items = getItemPage(pageable, itemName);
        model.addAttribute("categories", categories);
        model.addAttribute("items", items);
        model.addAttribute("direction", direction);
        model.addAttribute("sortProperty",pageable.getSort().toString().contains("name") ? "name": "price");
        model.addAttribute("banner",bannerService.findOne());


        return "item/itemMainList";
    }

    private Page<Item> getItemPage( Pageable pageable, String itemName) {
        if (itemName != null && !itemName.isEmpty()) {
            return itemService.searchItemsByItemName(pageable, itemName);
        } else {
            return itemService.itemActiveList(pageable);
        }
    }


}
