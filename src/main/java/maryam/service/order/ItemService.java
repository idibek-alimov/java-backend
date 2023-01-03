package maryam.service.order;

import lombok.RequiredArgsConstructor;
import maryam.controller.order.ItemHolder;
import maryam.data.order.ItemRepository;
import maryam.data.product.ProductRepository;
import maryam.data.user.UserRepository;
import maryam.models.order.Item;
import maryam.models.order.Order;
import maryam.models.product.Product;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor @Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;
    public Item addItem(Item item){
        return itemRepository.save(item);
    }
    public List<Item> addItems(List<ItemHolder> items, Order order) throws RuntimeException{
        List<Item> itemList = new ArrayList<>();
        Item newItem;
        for(ItemHolder item:items){
            Product product = productRepository.findById(item.getProduct()).get();
            newItem = new Item(product,item.getSize(),item.getAmount(),order);
            newItem = itemRepository.save(newItem);
            itemList.add(newItem);
        }
        order.setItems(itemList);
        return itemList;
    }
}
