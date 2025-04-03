package projlee.modules.dog.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;
import projlee.modules.banner.BannerService;
import projlee.modules.dog.domain.Dog;
import projlee.modules.dog.service.DogService;
import projlee.modules.item.service.ItemService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.rmi.server.LogStream.log;

@Controller
@RequiredArgsConstructor
@Getter
public class DogController {

    private static final Logger log = LoggerFactory.getLogger(DogController.class);
    private final DogService dogService;
    private final ItemService itemService;
    private final BannerService bannerService;

    @GetMapping("/dog/description/{id}")
    public String description(@CurrentAccount Account account, @PathVariable("id") Long id, Model model) {

       if (account != null) {

           Dog getDog = dogService.getDog(id);
           model.addAttribute("dogDescription", getDog);
           model.addAttribute(account);

           return "dog/description";
       }

       return "index";
   }

    @GetMapping("/dog/mainList")
    public String mainList(@CurrentAccount Account account,
                           @PageableDefault(size = 18, sort = "dogName", direction = Sort.Direction.DESC)
                           Pageable pageable, Model model){

        Page<Dog> dogs;

        if (account != null) {
            model.addAttribute(account);
            dogs = dogService.dogList(pageable);
            model.addAttribute("dogs", dogs);
            model.addAttribute("banner",bannerService.findOne());

            Set<String> uniqueBreeds = dogs.stream()
                    .map(Dog::getDogBreed)
                    .collect(Collectors.toSet());
            model.addAttribute("dogBreeds", uniqueBreeds);
            System.out.println("uniqueBreeds: " + uniqueBreeds);

            return "dog/dogMainList";
        }

        model.addAttribute("itemList",itemService.itemFirstPage());
        model.addAttribute("dogList", dogService.firstPage());

        return "index";
    }

    @GetMapping("/dog/{dogBreed}")
    public String dogBreedList(
            @CurrentAccount Account account,
            @PathVariable("dogBreed") String dogBreed,
            Model model
    ) {

        model.addAttribute("banner",bannerService.findOne());
        if (account != null) {
            List<Dog> dogsBreed = dogService.findByDogBreed(dogBreed);
            List<Dog> dogs = dogService.findAll();
            Set<String> uniqueBreeds = dogs.stream()
                    .map(Dog::getDogBreed)
                    .collect(Collectors.toSet());

            model.addAttribute("dogBreeds", uniqueBreeds); //sidebar
            model.addAttribute("dogsBreed", dogsBreed);
            model.addAttribute("account", account);
            model.addAttribute("dogs", dogs);


            return "dog/dogBreedList";
        }

        return "index";
    }
}
