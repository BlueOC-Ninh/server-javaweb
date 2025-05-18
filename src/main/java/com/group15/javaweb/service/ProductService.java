package com.group15.javaweb.service;
import com.group15.javaweb.dto.request.ProductFilterRequest;
import com.group15.javaweb.dto.request.ProductCreateRequest;
import com.group15.javaweb.dto.request.ProductUpdateRequest;
import com.group15.javaweb.entity.Category;
import com.group15.javaweb.entity.Product;
import com.group15.javaweb.exception.ApiException;
import com.group15.javaweb.repository.CategoryRepository;
import com.group15.javaweb.repository.ProductRepository;
import com.group15.javaweb.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Page<Product> getFilteredProducts(ProductFilterRequest filter) {
        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(Sort.Direction.fromString(filter.getSortDir()), filter.getSortBy())
        );

        return productRepository.findAll(
                ProductSpecification.filter(filter.getName(), filter.getCategoryId(), filter.getDeleted()),
                pageable
        );
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException(404, "Sản phẩm không tồn tại"));

        product.setDeleted(true);
        productRepository.save(product);
    }

    public void restoreProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException(404, "Sản phẩm không tồn tại"));

        product.setDeleted(false);
        productRepository.save(product);
    }

    public Product updateProduct(String productId, ProductUpdateRequest request) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(404, "Không tìm thấy sản phẩm"));

        Category category = categoryRepository.findById(request.getCategory_id())
                .orElseThrow(() -> new ApiException(400, "Không tìm thấy danh mục"));

        MultipartFile image = request.getAvatarUrl();
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            product.setAvatarUrl(imageUrl);
        }

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());
        product.setReceivingProcess(request.getReceivingProcess());
        product.setWarrantyPolicy(request.getWarrantyPolicy());
        product.setFrequentlyAskedQuestions(request.getFrequentlyAskedQuestions());
        product.setDiscount(request.getDiscount());
        product.setDaysValid(request.getDaysValid());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public Product getProductDetail(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ApiException(404, "Sản phẩm không tồn tại"));
    }

    public Product createProduct(ProductCreateRequest request) throws IOException {
        if (request.getAvatarUrl() == null || request.getAvatarUrl().isEmpty()) {
            throw  new ApiException(400, "Ảnh sản phẩm không được để trống");
        }


        Category category = categoryRepository.findById(request.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục sản phẩm"));

        MultipartFile image = request.getAvatarUrl();
        String imageUrl = image != null && !image.isEmpty()
                ? cloudinaryService.uploadImage(image)
                : null;

        Product product = new Product();
        product.setName(request.getName());
        product.setAvatarUrl(imageUrl);
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setSoldCount(request.getSoldCount());
        product.setDescription(request.getDescription());
        product.setReceivingProcess(request.getReceivingProcess());
        product.setWarrantyPolicy(request.getWarrantyPolicy());
        product.setFrequentlyAskedQuestions(request.getFrequentlyAskedQuestions());
        product.setDiscount(request.getDiscount());
        product.setDaysValid(request.getDaysValid());
        product.setCategory(category);

        return productRepository.save(product);
    }
}
