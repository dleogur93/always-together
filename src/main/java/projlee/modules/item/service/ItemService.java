package projlee.modules.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.item.domain.Item;
import projlee.modules.item.repository.ItemRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findAll() {
       return itemRepository.findAll();
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(()->new NoSuchElementException("Item not found"));
    }

    public List<Item> itemFirstPage() {

        return itemRepository.findTop12ByOrderByRegistrationDateDesc();
    }

    public Page<Item> itemList(Pageable pageable) {

       return itemRepository.findAll(pageable);
    }

    public Page<Item> searchItemsByItemName(Pageable pageable, String itemName) {

        return itemRepository.findByNameContainingIgnoreCase(pageable,itemName);
    }

    public Page<Item> findByCategoryId(Pageable pageable ,Long categoryId) {
        return itemRepository.findByCategoryId(pageable,categoryId);
    }
}
