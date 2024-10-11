package projlee.modules.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projlee.modules.account.AccountService;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;
import projlee.modules.dog.domain.Dog;
import projlee.modules.dog.service.DogService;
import projlee.modules.reservation.domain.DogReservation;
import projlee.modules.reservation.form.DogReservationForm;
import projlee.modules.reservation.service.DogReservationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Controller
@RequiredArgsConstructor
public class DogReservationController {

    private final DogReservationService dogReservationService;
    private final DogService dogService;
    private final AccountService accountService;
//////////
    @GetMapping("/reservation/dog/{id}")
    public String dogReservationForm(@PathVariable(name = "id") Long id, @CurrentAccount Account account,  Model model) {

        Dog dog = dogService.getDog(id);
        Account accountById = accountService.getAccountById(account.getId());

        model.addAttribute("dog", dog);
        model.addAttribute("account",accountById);
        model.addAttribute(new DogReservationForm());

        return "dog/dogReservation";

    }

    @PostMapping("/reservation/dog")
    public String dogReservation(@RequestParam("accountId") Long accountId,
                                 @RequestParam("dogId") Long dogId,
                                 @CurrentAccount Account account, @Valid DogReservationForm dogReservationForm, BindingResult result, Model model
                                ){

        if (result.hasErrors()) {
            model.addAttribute("account",account);
            model.addAttribute("dog", dogService.getDog(dogId));
            return "dog/dogReservation";
        }


        if (account != null) {

            String stringDateTime = dogReservationForm.getReservationDate() + "T" + dogReservationForm.getReservationTime();
            LocalDateTime reservationDateTime;


            try {
                reservationDateTime = LocalDateTime.parse(stringDateTime);
            } catch (DateTimeParseException e) {
                result.rejectValue("reservationDate", "invalid.date", "날짜와 시간이 잘 못 됐습니다.");
                model.addAttribute("account",account);
                model.addAttribute("dog", dogService.getDog(dogId));
                return "dog/dogReservation";
            }

            // Check if the reservation date is today or before
            if (reservationDateTime.isBefore(LocalDateTime.now())) {
                result.rejectValue("reservationDate", "invalid.date", "상담예약 날짜는 현재시각 이후로만 가능합니다.");
                model.addAttribute("account",account);
                model.addAttribute("dog", dogService.getDog(dogId));
                return "dog/dogReservation";
            }


            dogReservationService.createDogReservation(accountId,dogId,reservationDateTime);
            model.addAttribute("account",account);
            model.addAttribute("dog", dogService.getDog(dogId));
            return "redirect:/";
        }


        return "dog/dogReservation";
    }


    ////////
    @GetMapping("/account/{accountId}/reservationDetail")
    public String dogReservationDetailView(@PathVariable("accountId") String accountId, @CurrentAccount Account account, Model model) {


        Account getAccount = accountService.getAccount(accountId);
        DogReservation dogReservation = getAccount.getDogReservation();

        if (dogReservation == null) {

            model.addAttribute("hasReservation", false);

        }else {
            Dog dog = dogReservation.getDog();
            model.addAttribute("dogReservation", dogReservation);
            model.addAttribute("dog", dog);
            model.addAttribute("hasReservation", true);
        }


        model.addAttribute(getAccount);
        model.addAttribute(account);
        return "dog/reservationDetail";
    }

    @PostMapping("/account/reservationDetail/delete")
    public String reservationDelete(@CurrentAccount Account account) {

        Account getAccount = accountService.getAccount(account.getAccountId());
        DogReservation dogReservation = getAccount.getDogReservation();
        Dog getDog = dogReservation.getDog();


        if (dogReservation.getId() != null) {
            dogReservationService.delete(dogReservation);
            dogReservationService.accountDogReservationDelete(getAccount);
            dogReservationService.dogReservationDelete(getDog);

        }

        return "redirect:/";
    }

}
