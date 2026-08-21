package as.r_petals.service;

import as.r_petals.dto.FullProductRequest;
import as.r_petals.dto.category.CategoryResponse;
import as.r_petals.dto.product.ProductImageResponse;
import as.r_petals.dto.product.ProductResponse;
import as.r_petals.dto.product.ProductUpdateRequest;
import as.r_petals.dto.subcategory.SubCategoryResponse;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        Category category = categoryRepository.findByCategoryNameIgnoreCase(categoryName).orElseGet(() -> {
            Category c = new Category();
            c.setCategoryName(categoryName);
            c.setActive(true);
            c.setCreatedAt(LocalDateTime.now());
            c.setUpdatedAt(LocalDateTime.now());
            return categoryRepository.save(c);
        });

        SubCategory subCategory = subCategoryRepository.findByCategoryIdAndSubCategoryNameIgnoreCase(category.getId(), subCategoryName).orElseGet(() -> {
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
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        List<ProductImage> savedImages = new ArrayList<>();

        try {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                ImageUploadService.UploadResult upload = imageUploadService.uploadWithDetails(
                        image,
                        "r_petals/products/" + savedProduct.getId()
                );

                ProductImage productImage = new ProductImage();
                productImage.setProductId(savedProduct.getId());
                productImage.setImageUrl(upload.url());
                productImage.setPublicId(upload.publicId());
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
            product.setActive(request.getActive() );
        }

        product.setUpdatedAt(LocalDateTime.now());
        Product saved = productRepository.save(product);

        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId );

        // IMPORTANT:
        // category + subcategory bhi response mein bhejna
        return buildFullProductResponse(
                saved,
                images
        );
    }

//    delete product by id
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);

        for (ProductImage image : images) {
            String publicId = resolvePublicId(image);
            if (publicId != null) {
                imageUploadService.deleteByPublicId(publicId);
            }
        }

        productImageRepository.deleteByProductId(productId);
        productRepository.delete(product);
    }

//    delete SubCategory by id
    public void deleteSubCategory(String subCategoryId) {
        if (productRepository.existsBySubCategoryId(subCategoryId)) {
            throw new ConflictException("Subcategory contains products");
        }

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found"));

        subCategoryRepository.delete(subCategory);
    }

//    delete category by id
    public void deleteCategory(String categoryId) {
        if (subCategoryRepository.existsByCategoryId(categoryId)) {
            throw new ConflictException("Category contains subcategories");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);
    }

    private ProductResponse buildResponse(Product product, List<ProductImage> images) {
        ProductResponse response = new ProductResponse(product);
        List<ProductImageResponse> imageResponses = images.stream().map(
                image -> new ProductImageResponse(
                        image.getId(),
                        image.getProductId(),
                        image.getImageUrl(),
                        image.getDisplayOrder(),
                        image.isPrimary()
                )
        ).toList();
        response.setImages(imageResponses);
        return response;
    }

    public List<CategoryResponse> getCategoriesWithProductCount() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> response = new ArrayList<>();

        for (Category category : categories) {
            CategoryResponse categoryResponse = new CategoryResponse(category);

            List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(category.getId());
            List<SubCategoryResponse> subCategoryResponses = subCategories.stream()
                    .map(SubCategoryResponse::new)
                    .collect(Collectors.toList());
            categoryResponse.setSubCategories(subCategoryResponses);

            long productCount = 0;
            for (SubCategory subCategory : subCategories) {
                productCount += productRepository.countBySubCategoryId(subCategory.getId());
            }

            categoryResponse.setProductCount(productCount);
            response.add(categoryResponse);
        }
        return response;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> responses = new ArrayList<>();

        for (Product product : products) {
            List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(product.getId());
            ProductResponse response = buildResponse(product, images);

            SubCategory subCategory = subCategoryRepository.findById(product.getSubCategoryId()).orElse(null);
            if (subCategory != null) {
                response.setSubCategoryName(subCategory.getSubCategoryName());
                Category category = categoryRepository.findById(subCategory.getCategoryId()).orElse(null);
                if (category != null) {
                    response.setCategoryName(category.getCategoryName());
                }
            }
            responses.add(response);
        }
        return responses;
    }

    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);
        ProductResponse response = buildResponse(product, images);

        SubCategory subCategory = subCategoryRepository.findById(product.getSubCategoryId()).orElse(null);
        if (subCategory != null) {
            response.setSubCategoryName(subCategory.getSubCategoryName());
            Category category = categoryRepository.findById(subCategory.getCategoryId()).orElse(null);
            if (category != null) {
                response.setCategoryName(category.getCategoryName());
            }
        }
        return response;
    }

    public ProductResponse addProductImages(String productId, List<MultipartFile> images) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        validateImages(images);

        List<ProductImage> currentImages = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);

        if (currentImages.size() + images.size() > 6) {
            throw new IllegalArgumentException(
                    "A product can have maximum 6 images. You can add only "
                            + (6 - currentImages.size()) + " more image(s)."
            );
        }

        List<ProductImage> newImages = new ArrayList<>();
        int nextOrder = currentImages.size() + 1;

        try {
            for (MultipartFile image : images) {
                ImageUploadService.UploadResult upload = imageUploadService.uploadWithDetails(
                        image,
                        "r_petals/products/" + productId
                );

                ProductImage productImage = new ProductImage();
                productImage.setProductId(productId);
                productImage.setImageUrl(upload.url());
                productImage.setPublicId(upload.publicId());
                productImage.setDisplayOrder(nextOrder++);
                productImage.setPrimary(false);

                newImages.add(productImage);
            }

            productImageRepository.saveAll(newImages);
        } catch (Exception e) {
            for (ProductImage image : newImages) {
                try {
                    imageUploadService.deleteByPublicId(image.getPublicId());
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException("Product image add failed", e);
        }

        return getProductById(productId);
    }

    public ProductResponse replaceProductImage(String productId, String imageId, MultipartFile image) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        validateImages(List.of(image));

        ProductImage oldImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Product image not found"));

        if (!productId.equals(oldImage.getProductId())) {
            throw new ResourceNotFoundException("Product image not found");
        }

        ImageUploadService.UploadResult upload = imageUploadService.uploadWithDetails(
                image,
                "r_petals/products/" + productId
        );

        String oldPublicId = resolvePublicId(oldImage);

        try {
            oldImage.setImageUrl(upload.url());
            oldImage.setPublicId(upload.publicId());
            productImageRepository.save(oldImage);
        } catch (Exception e) {
            try {
                imageUploadService.deleteByPublicId(upload.publicId());
            } catch (Exception ignored) {
            }
            throw new RuntimeException("Product image replace failed", e);
        }

        if (oldPublicId != null && !oldPublicId.equals(upload.publicId())) {
            try {
                imageUploadService.deleteByPublicId(oldPublicId);
            } catch (Exception e) {
                System.err.println("Could not delete old Cloudinary image: " + e.getMessage());
            }
        }

        return getProductById(productId);
    }

    public ProductResponse deleteProductImage(String productId, String imageId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Product image not found"));

        if (!productId.equals(image.getProductId())) {
            throw new ResourceNotFoundException("Product image not found");
        }

        List<ProductImage> currentImages = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);

        if (currentImages.size() <= 1) {
            throw new ConflictException("Product must have at least one image");
        }

        String publicId = resolvePublicId(image);
        if (publicId != null) {
            imageUploadService.deleteByPublicId(publicId);
        }

        productImageRepository.delete(image);

        List<ProductImage> remaining = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);

        for (int i = 0; i < remaining.size(); i++) {
            remaining.get(i).setDisplayOrder(i + 1);
        }

        boolean hasPrimary = remaining.stream().anyMatch(ProductImage::isPrimary);
        if (!hasPrimary && !remaining.isEmpty()) {
            remaining.get(0).setPrimary(true);
        }

        productImageRepository.saveAll(remaining);
        return getProductById(productId);
    }

    private void validateImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("At least one product image is required");
        }

        if (images.size() > 6) {
            throw new IllegalArgumentException("Maximum 6 product images are allowed");
        }

        for (MultipartFile image : images) {
            if (image == null || image.isEmpty()) {
                throw new IllegalArgumentException("Image file is required");
            }

            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }
        }
    }

    private String resolvePublicId(ProductImage image) {
        if (image.getPublicId() != null && !image.getPublicId().isBlank()) {
            return image.getPublicId();
        }
        return extractCloudinaryPublicId(image.getImageUrl());
    }

    private String extractCloudinaryPublicId(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        try {
            int uploadIndex = imageUrl.indexOf("/upload/");
            if (uploadIndex < 0) {
                return null;
            }

            String path = imageUrl.substring(uploadIndex + "/upload/".length());
            String[] parts = path.split("/");

            int start = 0;
            if (parts.length > 0 && parts[0].matches("v\\d+")) {
                start = 1;
            }

            if (start >= parts.length) {
                return null;
            }

            StringBuilder publicId = new StringBuilder();
            for (int i = start; i < parts.length; i++) {
                if (publicId.length() > 0) {
                    publicId.append('/');
                }
                publicId.append(parts[i]);
            }

            int extensionIndex = publicId.lastIndexOf(".");
            if (extensionIndex > 0) {
                publicId.delete(extensionIndex, publicId.length());
            }

            return publicId.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private ProductResponse buildFullProductResponse(Product product,List<ProductImage> images) {

        ProductResponse response = buildResponse(product,images);
        // SUBCATEGORY
        SubCategory subCategory =subCategoryRepository.findById(product.getSubCategoryId()).orElse(null);


        if (subCategory != null) {

            response.setSubCategoryName(subCategory.getSubCategoryName());

            // CATEGORY
            Category category =categoryRepository.findById(subCategory.getCategoryId()).orElse(null);


            if (category != null) {
                response.setCategoryName( category.getCategoryName());
            }
        }

        
        return response;
    }
}