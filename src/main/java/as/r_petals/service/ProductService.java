package as.r_petals.service;

import as.r_petals.entities.Category;
import as.r_petals.entities.Product;
import as.r_petals.entities.SubCategory;
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

    public Product createFullProduct(
            String categoryName,
            String categoryImage,
            String subCategoryName,
            String productName,
            String description,
            Double price,
            String productImage
    ) {

        // 1) Category: pehle se hai to reuse, warna naya banao
        Category category = categoryRepository
                .findByCategoryNameIgnoreCase(categoryName)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(categoryName);
                    newCategory.setCategoryImage(categoryImage);
                    newCategory.setActive(true);
                    newCategory.setCreatedAt(LocalDateTime.now());
                    newCategory.setUpdatedAt(LocalDateTime.now());
                    return categoryRepository.save(newCategory);
                });

        // 2) SubCategory: isi category ke andar pehle se hai to reuse, warna naya banao
        SubCategory subCategory = subCategoryRepository
                .findByCategoryIdAndSubCategoryNameIgnoreCase(category.getId(), subCategoryName)
                .orElseGet(() -> {
                    SubCategory newSubCategory = new SubCategory();
                    newSubCategory.setCategoryId(category.getId());
                    newSubCategory.setSubCategoryName(subCategoryName);
                    newSubCategory.setActive(true);
                    newSubCategory.setCreatedAt(LocalDateTime.now());
                    newSubCategory.setUpdatedAt(LocalDateTime.now());
                    return subCategoryRepository.save(newSubCategory);
                });

        // 3) Duplicate product check
        if (productRepository.existsBySubCategoryIdAndProductNameIgnoreCase(subCategory.getId(), productName)) {
            throw new IllegalArgumentException(
                    "Product '" + productName + "' already exists in this subcategory");
        }

        // 4) Product save karo
        Product product = new Product();
        product.setSubCategoryId(subCategory.getId());
        product.setProductName(productName);
        product.setDescription(description);
        product.setPrice(price);
        product.setProductImage(productImage);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    // Update Product
    public Map<String, Object> updateProduct(String productId, Product request) {

        Map<String, Object> response = new HashMap<>();

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            response.put("success", false);
            response.put("message", "Product Not Found");
            return response;
        }

        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setProductImage(request.getProductImage());
        product.setActive(request.isActive());
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);

        response.put("success", true);
        response.put("message", "Product Updated Successfully");
        response.put("product", product);

        return response;
    }

    // Delete Product
    public Map<String, Object> deleteProduct(String productId) {

        Map<String, Object> response = new HashMap<>();

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {

            response.put("success", false);
            response.put("message", "Product Not Found");

            return response;
        }

        productRepository.delete(product);

        response.put("success", true);
        response.put("message", "Product Deleted Successfully");

        return response;
    }

    // Delete SubCategory
    public Map<String, Object> deleteSubCategory(String subCategoryId) {

        Map<String, Object> response = new HashMap<>();

        if (productRepository.existsBySubCategoryId(subCategoryId)) {

            response.put("success", false);
            response.put("message", "SubCategory contains products.");

            return response;
        }

        SubCategory subCategory =
                subCategoryRepository.findById(subCategoryId).orElse(null);

        if (subCategory == null) {

            response.put("success", false);
            response.put("message", "SubCategory Not Found");

            return response;
        }

        subCategoryRepository.delete(subCategory);

        response.put("success", true);
        response.put("message", "SubCategory Deleted");

        return response;
    }

    // Delete Category
    public Map<String, Object> deleteCategory(String categoryId) {

        Map<String, Object> response = new HashMap<>();

        if (subCategoryRepository.existsByCategoryId(categoryId)) {

            response.put("success", false);
            response.put("message", "Category contains SubCategories.");

            return response;
        }

        Category category =
                categoryRepository.findById(categoryId).orElse(null);

        if (category == null) {

            response.put("success", false);
            response.put("message", "Category Not Found");

            return response;
        }

        categoryRepository.delete(category);

        response.put("success", true);
        response.put("message", "Category Deleted");

        return response;
    }
}