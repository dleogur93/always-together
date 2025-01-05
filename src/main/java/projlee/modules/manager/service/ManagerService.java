package projlee.modules.manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.account.domain.Account;
import projlee.modules.dog.domain.Dog;
import projlee.modules.dog.form.DogRegistration;
import projlee.modules.dog.repository.DogRepository;
import projlee.modules.item.domain.Category;
import projlee.modules.item.domain.Food;
import projlee.modules.item.domain.Item;
import projlee.modules.item.form.FoodForm;
import projlee.modules.item.form.ItemModifyForm;
import projlee.modules.item.repository.CategoryRepository;
import projlee.modules.item.repository.ItemRepository;
import projlee.modules.item.service.ItemService;
import projlee.modules.manager.form.DogModifyForm;
import projlee.modules.order.domain.Order;
import projlee.modules.order.repository.OrderRepository;
import projlee.modules.reservation.domain.DogReservation;
import projlee.modules.reservation.repository.DogReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ManagerService {

    private final DogRepository dogRepository;
    private final DogReservationRepository dogReservationRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    public void dogRegistration(DogRegistration dogRegistration) {
        getDog(dogRegistration);

    }

    private void getDog(DogRegistration dogRegistration) {

        Dog dog = Dog.builder()
                .dogBreed(dogRegistration.getDogBreed())
                .dogName(dogRegistration.getDogName())
                .dogAge(dogRegistration.getDogAge())
                .dogSex(dogRegistration.getDogSex())
                .information(dogRegistration.getInformation())
                .dogPicture(dogRegistration.getDogPicture())
                .dogRegistrationDate(LocalDate.now())
                .adoption(false)
                .adopterName("X")
                .build();

        dogRepository.save(dog);
    }

    public Page<Dog> searchDogsByDogName(Pageable pageable, String dogName) {
       return dogRepository.findByDogName(pageable,dogName);
    }

    public Page<Dog> dogList(Pageable pageable) {

        return dogRepository.findAll(pageable);

    }



    public void dogDelete(Long id) {

        dogRepository.deleteById(id);
    }

    public void modifyDog(Dog dog, DogModifyForm dogModifyForm) {

      dog.dogModify(dogModifyForm);
      dogRepository.save(dog);
    }

    public Dog getModifyDogId(Long id) {
      return  dogRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Dog not found with id: " + id));
    }

    public Page<DogReservation> findDogReservationsByReservationTime(LocalDateTime dateTime, Pageable pageable) {
        return dogReservationRepository.findDogReservationsByReservationTime(dateTime,pageable);
    }


    public Page<DogReservation> dogRegistrationList(Pageable pageable) {
      return  dogReservationRepository.findAll(pageable);
    }

    public List<DogReservation> getReservationsByDate(LocalDate today) {
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return dogReservationRepository.findByReservationTimeBetween(startOfDay, endOfDay);
    }


    public void dogReservationDelete(Long id) {
        DogReservation dogReservation = dogReservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dog reservation not found"));

        dogReservationRepository.deleteById(id);
        accountDogReservationDelete(dogReservation.getAccount());
        getDogReservationDelete(dogReservation.getDog());
    }

    private void getDogReservationDelete(Dog dog) {
        dog.dogReservationDelete();
    }

    private void accountDogReservationDelete(Account account) {
        account.accountDogReservationDelete();
    }


    public void dogAdoptionConfirmed(Long id) {
        DogReservation dogReservation = dogReservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dog reservation not found"));

        dogReservationRepository.deleteById(id);
        accountDogReservationDelete(dogReservation.getAccount());
        getDogReservationDelete(dogReservation.getDog());

        adoptionConfirmed(dogReservation.getDog(),dogReservation.getAccount().getName());


    }

    private void adoptionConfirmed(Dog dog, String name) {
        dog.adoptionConfirmed(name);
    }

    /**
     *  입양된 유기견 리스트
     */
    public Page<Dog> dogAdoptionList(Pageable pageable) {
        return dogRepository.findByAdoptionTrue(pageable);
    }


    /**
     *   item food 등록
     */
    public void foodRegistration(FoodForm form) {

        Food food = createFood(form);

        for (Long categoryId : form.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            category.addItem(food);
        }

        // Food 객체 저장
        itemRepository.save(food);


    }

    private static Food createFood(FoodForm form) {

        return Food.builder()
                .name(form.getName())
                .price(form.getPrice())
                .stockQuantity(form.getStockQuantity())
                .itemPicture(form.getItemPicture())
                .itemInformation(form.getItemInformation())
                .registrationDate(LocalDateTime.now())
                .expirationDate(form.getExpirationDate())
                .build();

    }

    public Page<Item> itemList(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Page<Item> itemActiveList(Pageable pageable) {

        return itemRepository.findAllActive(pageable);
    }


    public Page<Item> searchItemsByName(Pageable pageable, String name) {
        return itemRepository.findByNameContainingIgnoreCase(pageable,name);
    }

    public Item findByItem(Long id) {
       return itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("food not found " + id));
    }

    //== 아이템 수정 ==
    public void modifyItem(ItemModifyForm form, Long id) {

        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + id));

        item.updateDetails(form);
        // 새로운 카테고리 리스트 생성
        List<Category> newCategories = form.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + categoryId)))
                .collect(Collectors.toList());

        // 기존 카테고리 리스트와 새로운 카테고리 리스트 비교 후 업데이트
        List<Category> existingCategories = item.getCategories();
        List<Category> categoriesToRemove = new ArrayList<>(existingCategories);

        for (Category newCategory : newCategories) {
            if (!existingCategories.contains(newCategory)) {
                item.addCategory(newCategory);
            } else {
                categoriesToRemove.remove(newCategory);
            }
        }

        // 기존에 있지만 새로운 카테고리 리스트에 없는 카테고리 제거
        for (Category categoryToRemove : categoriesToRemove) {
            item.removeCategory(categoryToRemove);
        }

    }

    //== 아이템 삭제 ==
    public void itemDelete(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));

        if (item.getStockQuantity() > 0) {
            throw new IllegalArgumentException("이 상품은 재고가 남아 있어 삭제할 수 없습니다.");
        }

        item.softDelete();
        itemRepository.save(item);
    }
    // 아이템과 연결된 카테고리들에서 아이템 제거
//        for (Category category : new ArrayList<>(item.getCategories())) {
//            category.removeItem(item);
//        }

    // 아이템 삭제
//        itemRepository.delete(item);
}
