package projlee.modules.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projlee.modules.account.AccountService;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;
import projlee.modules.banner.Banner;
import projlee.modules.banner.BannerForm;
import projlee.modules.banner.BannerService;
import projlee.modules.banner.BannerUpdateForm;
import projlee.modules.dog.domain.Dog;
import projlee.modules.dog.form.DogRegistration;
import projlee.modules.item.domain.Category;
import projlee.modules.item.domain.Item;
import projlee.modules.item.form.FoodForm;
import projlee.modules.item.form.ItemModifyForm;
import projlee.modules.item.service.CategoryService;
import projlee.modules.manager.form.DogModifyForm;
import projlee.modules.manager.service.ManagerService;
import projlee.modules.reservation.domain.DogReservation;
import projlee.modules.reservation.service.DogReservationService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final CategoryService categoryService;
    private final BannerService bannerService;

    @GetMapping("/manager")
    public String manager() {
        return "manager/manager";
    }

    @GetMapping("/manager/dogRegistration")
    public String managerDogRegistrationForm(Model model) {

       model.addAttribute(new DogRegistration());

        return "manager/dogRegistration";
    }


    /**
     * 유기견 등록
     */
    @PostMapping("/manager/dogRegistration")
    public String managerDogRegistration(@Valid DogRegistration dogRegistration, BindingResult result, RedirectAttributes attributes
                                        ) {

        if (result.hasErrors()) {
            return "manager/dogRegistration";
        }

        managerService.dogRegistration(dogRegistration);


        attributes.addFlashAttribute("dog","등록 완료했습니다.");

        return "redirect:/manager/dogRegistration";
    }


    /**
     * 유기견 전체 목록 페이지
     */
    @GetMapping("/manager/dogList")
    public String managerDogList(@PageableDefault(size = 9, sort = "dogName", direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(name = "dogName", required = false ) String dogName, Model model) {

        Page<Dog> dogs;
        if ( dogName != null && !dogName.isEmpty()) {
            dogs = managerService.searchDogsByDogName(pageable , dogName);
        } else {
            dogs = managerService.dogList(pageable);
        }

        model.addAttribute("dogs", dogs);
        model.addAttribute("sortProperty",pageable.getSort().toString().contains("dogName") ? "dogName": "dogBreed");

        return "manager/dogList";
    }



    @GetMapping("/manager/dogModify/{id}")
    public String managerDogModifyForm(@PathVariable("id") Long id, Model model) {

        Dog dog = managerService.getModifyDogId(id);
        model.addAttribute(new DogModifyForm(dog));

        return "manager/dogModify";
    }

    @PostMapping("/manager/dogModify/{id}")
    public String managerDogModify(@PathVariable("id") Long id,@Valid DogModifyForm dogModifyForm,Model model
                                   ,BindingResult result ,RedirectAttributes attributes) {


        if (result.hasErrors()) {
            return "manager/dogModify/" + id;
        }

        Dog dog = managerService.getModifyDogId(id);
        model.addAttribute("dog",dog);
        managerService.modifyDog(dog,dogModifyForm);


        attributes.addFlashAttribute("message", "수정 완료");

        return "redirect:/manager/dogList";
    }

    /**
     * 유기견 상세정보
     */
    @GetMapping("/manager/dogInformation/{id}")
    public String dogInformation(@PathVariable("id") Long id, Model model) {

        Dog dog = managerService.getModifyDogId(id);
        model.addAttribute("dog", dog);

        return "manager/dogInformation";
    }


    /**
     * 유기견 삭제
     */
    @PostMapping("/manager/dogDelete")
    public String dogDelete(@RequestParam("id") Long id) {

        managerService.dogDelete(id);

        return "redirect:/manager/dogList";

    }


    /**
     *  유기견 상담 일정
     */

    @GetMapping("manager/dogConsulting")
    public String dogConsultingView(@PageableDefault(size = 9, sort = "reservationTime", direction = Sort.Direction.ASC) Pageable pageable,
                                    Model model ) {

        Page<DogReservation> dogReservations =  managerService.dogRegistrationList(pageable);

        LocalDate today = LocalDate.now();
        List<DogReservation> todayDogReservation = managerService.getReservationsByDate(today);

        model.addAttribute("today",today);
        model.addAttribute("todayDogReservations", todayDogReservation);
        model.addAttribute("dogReservations",dogReservations);
        return "manager/dogConsulting";
    }

    @PostMapping("/manager/dogReservationDelete")
    public String dogReservationDelete(@RequestParam("id") Long id, RedirectAttributes attributes) {

        managerService.dogReservationDelete(id);

        attributes.addFlashAttribute("message","예약 취소 완료");

        return "redirect:/manager/dogConsulting";
    }

    @PostMapping("/manager/dogAdoptionConfirmed")
    public String dogAdoptionConfirmed(@RequestParam("id") Long id, RedirectAttributes attributes) {

        managerService.dogAdoptionConfirmed(id);

        attributes.addFlashAttribute("message","유기견 입양 확정");
        return "redirect:/manager/dogConsulting";
    }

    @GetMapping("/manager/dogAdoptionList")
    public String dogAdoptionList(@PageableDefault(size = 9, direction = Sort.Direction.DESC) Pageable pageable,Model model) {

        Page<Dog> dogs;
        dogs = managerService.dogAdoptionList(pageable);

        model.addAttribute("dogs",dogs);
        return "manager/dogAdoptionList";

    }


    //////////////////Item Food


    @GetMapping("/manager/foodRegistration")
    public String foodRegistrationForm(Model model) {

        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("foodForm", new FoodForm());

        return "manager/foodRegistrationForm";
    }

    @PostMapping("/manager/foodRegistration")
    public String foodRegistration(@Valid FoodForm foodForm, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            return "manager/foodRegistrationForm";
        }

        managerService.foodRegistration(foodForm);
        attributes.addFlashAttribute("item","상품 등록 완료했습니다.");

        return "redirect:/manager/foodRegistration";
    }

    @GetMapping("manager/itemList")
    public String foodList(@PageableDefault(size = 9, sort = "name", direction = Sort.Direction.DESC) Pageable pageable,
                           @RequestParam(name = "name", required = false ) String name,
                           Model model) {


        Page<Item> items;

        if (name != null && !name.isEmpty()) {
            items = managerService.searchItemsByName(pageable,name);
        } else {
            items = managerService.itemList(pageable);
        }

        model.addAttribute("items",items);
        model.addAttribute("sortProperty",pageable.getSort().toString().contains("name") ? "name": "category");

        return "manager/itemList";
    }

    @GetMapping("/manager/foodModify/{id}")
    public String foodModifyForm(@PathVariable("id") Long id, Model model) {

        Item food =  managerService.findByItem(id);
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("itemModifyForm",new ItemModifyForm(food));
        model.addAttribute("categories", categories);
        return "manager/foodModifyForm";
    }

    @PostMapping("/manager/foodModify/{id}")
    public String foodModify(@CurrentAccount Account account, @PathVariable("id") Long id,@ModelAttribute("itemModifyForm") ItemModifyForm itemModifyForm, BindingResult result, RedirectAttributes attributes
                            , Model model) {


        model.addAttribute(account);
        model.addAttribute(itemModifyForm);

        if (result.hasErrors()) {
            return "manager/foodModify/" + id;
        }

        managerService.modifyItem(itemModifyForm,id);
        attributes.addFlashAttribute("message", "수정 완료");

        return "redirect:/manager/itemList";
    }

    @PostMapping("/manager/itemDelete")
    public String itemDelete(@RequestParam("id") Long id, RedirectAttributes attributes) {

        managerService.itemDelete(id);

        attributes.addFlashAttribute("message","삭제 완료");

        return "redirect:/manager/itemList";
    }


    @GetMapping("/manager/banner")
    public String bannerForm(@CurrentAccount Account account, Model model){


        model.addAttribute("account",account);
        model.addAttribute(new BannerForm());

        return "manager/banner";
    }

    @PostMapping("/manager/banner")
    public String banner(@CurrentAccount Account account,@Valid BannerForm bannerForm,RedirectAttributes attributes,Model model){

        bannerService.createBanner(bannerForm);
        model.addAttribute("account",account);
        attributes.addFlashAttribute("message", "배너 이미지를 등록 했습니다.");

        return "redirect:/manager/banner";
    }

    @GetMapping("/manager/bannerUpdate")
    public String bannerUpdateForm(@CurrentAccount Account account,Model model){

        Banner banner = bannerService.findOne();

        model.addAttribute("account",account);
        model.addAttribute(new BannerUpdateForm(banner));

        return "manager/bannerUpdate";
    }

    @PostMapping("/manager/bannerUpdate")
    public String bannerUpdate(@CurrentAccount Account account,
                               @Valid BannerUpdateForm bannerUpdateForm, Errors errors, Model model,
                               RedirectAttributes attributes ){


        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "manager/bannerUpdate";
        }

        Banner banner = bannerService.findOne();

        bannerService.updateBanner(banner,bannerUpdateForm);
        model.addAttribute("bannerUpdateForm",bannerUpdateForm);
        model.addAttribute("account",account);
        attributes.addFlashAttribute("message", "배너 이미지를 업데이트했습니다.");

        return "redirect:/manager/bannerUpdate";
    }


}

