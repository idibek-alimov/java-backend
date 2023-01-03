package maryam.service.article;

import lombok.RequiredArgsConstructor;
import maryam.data.picture.PictureRepository;
import maryam.data.product.ArticleRepository;
import maryam.models.inventory.Inventory;
import maryam.models.product.Article;
import maryam.models.product.Color;
import maryam.models.product.Product;
import maryam.service.color.ColorService;
import maryam.service.inventory.InventoryService;
import maryam.service.picture.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor @Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final InventoryService inventoryService;
    private final PictureService pictureService;
    private final ColorService colorService;
    public Article addArticle(List<Inventory> inventories, List<MultipartFile> pictures, Color color, Product product){
        Article article = articleRepository.save(new Article(product));
        article.setPictures(pictureService.addPictures(pictures,article));
        article.setInventory(inventoryService.addInventories(inventories,article));
        System.out.println("before color thing");
        article.setColor(colorService.addColor(color,article));
        System.out.println("before retuning article 112232");
        return article;
    }
    public Article addArticle(List<Inventory> inventories, Color color, Product product){
        Article article = articleRepository.save(new Article(product));
        article.setInventory(inventoryService.addInventories(inventories,article));
        article.setColor(colorService.addColor(color,article));
        return article;
    }
}
