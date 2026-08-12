package as.r_petals.service;

import as.r_petals.dto.FullProductRequest;
import as.r_petals.dto.product.ProductImageResponse;
import as.r_petals.dto.product.ProductResponse;
import as.r_petals.dto.product.ProductUpdateRequest;
import as.r_petals.entities.Category;
import as.r_petals.entities.Product;
import as.r_petals.entities.ProductImage;
import as.r_petals.entities.SubCategory;
import as.r_petals.exception.ConflictException;
import as.r_petals.exception.ResourceNotFoundException;
import as.r_petals.repository.CategoryRepository;
import as.r_petals.repository.ProductImageRepository;
import as.r_petals.repository.ProductRepository;
import as.r_petals.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ImageUploadService imageUploadService;

    // CREATE PRODUCT + MULTIPLE IMAGES

    public ProductResponse createFullProduct(FullProductRequest request, List<MultipartFile> images) {

        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("At least one product image is required");
        }

        if (images.size() > 6) {
            throw new IllegalArgumentException("Maximum 6 product images are allowed");
        }

        String categoryName = request.getCategoryName().trim();

        String subCategoryName = request.getSubCategoryName().trim();

        String productName = request.getProductName().trim();

        // CATEGORY

        Category category = categoryRepository.findByCategoryNameIgnoreCase(categoryName).orElseGet(() -> {

            Category c = new Category();
            c.setCategoryName(categoryName);
            c.setActive(true);
            c.setCreatedAt(LocalDateTime.now());
            c.setUpdatedAt(LocalDateTime.now());
            return categoryRepository.save(c);
        });

        // SUBCATEGORY

        SubCategory subCategory = subCategoryRepository.findByCategoryIdAndSubCategoryNameIgnoreCase(category.getId(), subCategoryName).orElseGet(() -> {

            SubCategory s = new SubCategory();
            s.setCategoryId(category.getId());
            s.setSubCategoryName(subCategoryName);
            s.setActive(true);
            s.setCreatedAt(LocalDateTime.now());
            s.setUpdatedAt(LocalDateTime.now());
            return subCategoryRepository.save(s);
        });


        // DUPLICATE PRODUCT

        if (productRepository.existsBySubCategoryIdAndProductNameIgnoreCase(subCategory.getId(), productName)) {
            throw new ConflictException("Product already exists in this subcategory");
        }

        // CREATE PRODUCT

        Product product = new Product();
        product.setSubCategoryId(subCategory.getId());
        product.setProductName(productName);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        // UPLOAD ALL IMAGES

        List<ProductImage> savedImages = new ArrayList<>();

        try {

            for (int i = 0; i < images.size(); i++) {

                MultipartFile image = images.get(i);
                String imageUrl = imageUploadService.upload(image, "r_petals/products/" + savedProduct.getId());
                ProductImage productImage = new ProductImage();
                productImage.setProductId(savedProduct.getId());
                productImage.setImageUrl(imageUrl);
                productImage.setDisplayOrder(i + 1);
                productImage.setPrimary(i == 0);

                savedImages.add(productImage);
            }

            productImageRepository.saveAll(savedImages);

        } catch (Exception e) {
            productRepository.delete(savedProduct);

            throw new RuntimeException("Product image upload failed", e);
        }

        return buildResponse(savedProduct, savedImages);
    }

    // UPDATE PRODUCT

    public ProductResponse updateProduct(String productId, ProductUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (request.getProductName() != null) {
            product.setProductName(request.getProductName().trim());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }

        product.setUpdatedAt(LocalDateTime.now());
        Product saved = productRepository.save(product);

        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);
        return buildResponse(saved, images);
    }

    // DELETE PRODUCT

    public void deleteProduct(String productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productImageRepository.deleteByProductId(productId);
        productRepository.delete(product);
    }

    // DELETE SUBCATEGORY

    public void deleteSubCategory(String subCategoryId) {

        if (productRepository.existsBySubCategoryId(subCategoryId)) {
            throw new ConflictException("Subcategory contains products");
        }

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("Subcategory not found"));

        subCategoryRepository.delete(subCategory);
    }

    // DELETE CATEGORY

    public void deleteCategory(String categoryId) {

        if (subCategoryRepository.existsByCategoryId(categoryId)) {
            throw new ConflictException("Category contains subcategories");
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);
    }

    // RESPONSE BUILDER

    private ProductResponse buildResponse(Product product, List<ProductImage> images) {

        ProductResponse response = new ProductResponse(product);
        List<ProductImageResponse> imageResponses = images.stream().map(
                image -> new ProductImageResponse(
                        image.getId(),
                        image.getProductId(),
                        image.getImageUrl(),
                        image.getDisplayOrder(),
                        image.isPrimary())).toList();
        response.setImages(imageResponses);
        return response;
    }
}