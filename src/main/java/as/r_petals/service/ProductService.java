package as.r_petals.service;

import as.r_petals.dto.FullProductRequest;
import as.r_petals.dto.product.ProductResponse;
import as.r_petals.dto.product.ProductUpdateRequest;
import as.r_petals.entities.Category;
import as.r_petals.entities.Product;
import as.r_petals.entities.SubCategory;
import as.r_petals.exception.ConflictException;
import as.r_petals.exception.ResourceNotFoundException;
import as.r_petals.repository.CategoryRepository;
import as.r_petals.repository.ProductRepository;
import as.r_petals.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public ProductResponse createFullProduct(FullProductRequest request) {
        String categoryName = request.getCategoryName().trim();
        String subCategoryName = request.getSubCategoryName().trim();
        String productName = request.getProductName().trim();

        Category category = categoryRepository
                .findByCategoryNameIgnoreCase(categoryName)
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setCategoryName(categoryName);
                    c.setCategoryImage(request.getCategoryImage());
                    c.setActive(true);
                    c.setCreatedAt(LocalDateTime.now());
                    c.setUpdatedAt(LocalDateTime.now());
                    return categoryRepository.save(c);
                });

        SubCategory subCategory = subCategoryRepository
                .findByCategoryIdAndSubCategoryNameIgnoreCase(category.getId(), subCategoryName)
                .orElseGet(() -> {
                    SubCategory s = new SubCategory();
                    s.setCategoryId(category.getId());
                    s.setSubCategoryName(subCategoryName);
                    s.setActive(true);
                    s.setCreatedAt(LocalDateTime.now());
                    s.setUpdatedAt(LocalDateTime.now());
                    return subCategoryRepository.save(s);
                });

        if (productRepository.existsBySubCategoryIdAndProductNameIgnoreCase(subCategory.getId(), productName)) {
            throw new ConflictException("Product already exists in this subcategory");
        }

        Product product = new Product();
        product.setSubCategoryId(subCategory.getId());
        product.setProductName(productName);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setProductImage(request.getProductImage());
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return new ProductResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(String productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (request.getProductName() != null) {
            String name = request.getProductName().trim();
            product.setProductName(name);
        }
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getProductImage() != null) product.setProductImage(request.getProductImage());
        if (request.getActive() != null) product.setActive(request.getActive());
        product.setUpdatedAt(LocalDateTime.now());

        return new ProductResponse(productRepository.save(product));
    }

    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    public void deleteSubCategory(String subCategoryId) {
        if (productRepository.existsBySubCategoryId(subCategoryId)) {
            throw new ConflictException("Subcategory contains products");
        }
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found"));
        subCategoryRepository.delete(subCategory);
    }

    public void deleteCategory(String categoryId) {
        if (subCategoryRepository.existsByCategoryId(categoryId)) {
            throw new ConflictException("Category contains subcategories");
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }
}