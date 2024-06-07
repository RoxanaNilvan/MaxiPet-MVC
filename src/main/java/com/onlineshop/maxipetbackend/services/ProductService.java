package com.onlineshop.maxipetbackend.services;

import com.onlineshop.maxipetbackend.constants.ProductLogger;
import com.onlineshop.maxipetbackend.dtos.ProductDTO;
import com.onlineshop.maxipetbackend.dtos.ReviewsDTO;
import com.onlineshop.maxipetbackend.dtos.mappers.ProductMapper;
import com.onlineshop.maxipetbackend.dtos.mappers.ReviewsMapper;
import com.onlineshop.maxipetbackend.entities.Category;
import com.onlineshop.maxipetbackend.entities.Product;
import com.onlineshop.maxipetbackend.entities.Reviews;
import com.onlineshop.maxipetbackend.repositories.CategoryRepository;
import com.onlineshop.maxipetbackend.repositories.ProductRepository;
import com.onlineshop.maxipetbackend.repositories.ReviewsRepository;
import com.onlineshop.maxipetbackend.validators.ProductValidators;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewsRepository reviewsRepository;

    /**
     * Returns a list of ProductDTO objects representing the products.
     * @return a list of ProductDTO objects
     */
    public List<ProductDTO> findProducts(){
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(ProductMapper::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of ProductDTO objects representing the products in a specific category.
     * @param categoryName the name of the category
     * @param categoryAnimal the animal associated with the category
     * @return a list of ProductDTO objects in the specified category
     */
    public List<ProductDTO> findProductsByCategory(String categoryName, String categoryAnimal){
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> productDTOList = productList.stream().map(ProductMapper::toProductDTO).collect(Collectors.toList());
        List <ProductDTO> finalProduct = new ArrayList<>();
        for(ProductDTO productDTO : productDTOList){
            if(productDTO.getCategoryName().equals(categoryName) && productDTO.getCategoryAnimal().equals(categoryAnimal)){
                finalProduct.add(productDTO);
            }
        }
        return finalProduct;
    }

    /**
     * Sorts the products by price either in ascending or descending order.
     * @param method the sorting method ("ascending" or "descending")
     * @return a list of ProductDTO objects sorted by price
     */
    public List<ProductDTO> sortProductsByPrice(String method){
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> productDTOList = productList.stream().map(ProductMapper::toProductDTO).collect(Collectors.toList());
        if(method.equals("ascending")){
            Collections.sort(productDTOList, new Comparator<ProductDTO>() {
                @Override
                public int compare(ProductDTO p1, ProductDTO p2) {
                    return Double.compare(p1.getPrice(), p2.getPrice());
                }
            });
        }
        if(method.equals("descending")){
            Collections.sort(productDTOList, new Comparator<ProductDTO>() {
                @Override
                public int compare(ProductDTO p1, ProductDTO p2) {
                    return Double.compare(p2.getPrice(), p1.getPrice());
                }
            });
        }
        return productDTOList;
    }

    /**
     * Finds products with similar categories based on the given product ID.
     * @param productId the ID of the product
     * @return a list of ProductDTO objects with similar categories
     */
    public List<ProductDTO>findSimilarCategory(String productId){
        Product product = productRepository.findById(productId).get();
        List<ProductDTO> productDTOList = findProductsByCategory(product.getCategory().getName(), product.getCategory().getAnimal());
        List<ProductDTO> similarProducts = productDTOList.stream()
                .filter(p -> !p.getId().equals(productId))
                .limit(3)
                .collect(Collectors.toList());

        return similarProducts;
    }

    /**
     * Returns the product with the specified ID.
     * @param id the ID of the product to find
     * @return the ProductDTO object representing the found product
     */
    public ProductDTO findProductById(String id){
        Optional<Product> productOptional = productRepository.findById(id);
        if(!productOptional.isPresent()){
            LOGGER.error(ProductLogger.PRODUCT_WITH_ID_NOT_FOUND, id);
        }
        return ProductMapper.toProductDTO(productOptional.get());
    }

    /**
     * Inserts a new product into the database.
     * @param productDTO the ProductDTO object containing information about the product to insert
     * @return the ID of the inserted product
     */
    public String insert(ProductDTO productDTO){
        Product product = ProductMapper.toEntity(productDTO);
        if (!ProductValidators.validateProduct(product)) {
            LOGGER.error("Produs invalid: prețul sau stocul este negativ.");
            return null;
        }
        Category category = categoryRepository.findByAnimalAndName(productDTO.getCategoryAnimal(), productDTO.getCategoryName());
        if (category != null) {
            product.setCategory(category);
            product = productRepository.save(product);
            LOGGER.info(ProductLogger.PRODUCT_WITH_ID_INSERTED, product.getId());
            return product.getId();
        } else {
            LOGGER.error("Categorie inexistentă pentru produsul introdus: {} pentru animalul {}", productDTO.getCategoryName(), productDTO.getCategoryAnimal());
            return null;
        }
    }

    /**
     * Updates a product in the database.
     * @param id the ID of the product to update
     * @param productDTO the ProductDTO object containing the new information for the product to update
     * @return the ProductDTO object representing the updated product
     */
    public ProductDTO update(String id, ProductDTO productDTO, MultipartFile image){
        Optional<Product> productOptional = productRepository.findById(id);
        if(!productOptional.isPresent()){
            LOGGER.error(ProductLogger.PRODUCT_WITH_ID_NOT_FOUND, id);
            return null;
        }else{
            Product existingProduct = productOptional.get();
            if (productDTO.getName() != null && !productDTO.getName().isEmpty()) {
                existingProduct.setName(productDTO.getName());
            }

            if (productDTO.getPrice() != 0){
                existingProduct.setPrice(productDTO.getPrice());
            }

            if(productDTO.getStock() != 0){
                existingProduct.setStock(productDTO.getStock());
            }

            if (!image.isEmpty()) {
                try {
                    String filename = image.getOriginalFilename();
                    Path imagePath = Paths.get("src/main/resources/static/" + filename);
                    Files.createDirectories(imagePath.getParent());
                    Files.write(imagePath, image.getBytes());
                    existingProduct.setImageUrl("/static/" + filename);
                } catch (IOException e) {
                    LOGGER.error("Eroare la încărcarea imaginii", e);
                    return null;
                }
            }
            existingProduct = productRepository.save(existingProduct);
            LOGGER.info(ProductLogger.PRODUCT_WITH_ID_UPDATED, existingProduct.getId());
            return ProductMapper.toProductDTO(existingProduct);
        }
    }

    /**
     * Șterge un produs din baza de date.
     * @param id ID-ul produsului de șters
     */
    public int delete(String id){
        Optional<Product> productOptional = productRepository.findById(id);
        if(!productOptional.isPresent()){
            LOGGER.error(ProductLogger.PRODUCT_WITH_ID_NOT_FOUND, id);
            return -1;
        }else {
            productRepository.deleteById(id);
            LOGGER.info(ProductLogger.PRODUCT_WITH_ID_DELETED, id);
            return 0;
        }
    }

    /**
     * Retrieves all category names from the database.
     * @return a list of category names
     */
    public List<String> getAllCategoryNames() {
        return categoryRepository.findAllCategoryNames();
    }

    /**
     * Retrieves all animal types from the database.
     * @return a list of animal types
     */
    public List<String> getAllAnimalTypes() {
        return categoryRepository.findAllAnimalTypes();
    }

    /**
     * Searches for products based on the provided search query.
     * @param searchQuery the search query to match against product names
     * @return a list of ProductDTO objects matching the search query
     */
    public List<ProductDTO> searchProducts(String searchQuery) {
        List<Product> productList = productRepository.findByNameContainingIgnoreCase(searchQuery);
        return productList.stream()
                .map(ProductMapper::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all reviews for a specific product.
     * @param productId the ID of the product to retrieve reviews for
     * @return a list of ReviewsDTO objects representing the reviews for the product
     */
    public List<ReviewsDTO> findAllReviewsForProduct(String productId){
        List<Reviews> reviews = reviewsRepository.findByProduct_Id(productId);
        return reviews.stream()
                .map(ReviewsMapper::toReviewsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Searches for products based on specified criteria.
     *
     * @param animalTypes   the type of animal for which products are being searched
     * @param animalAges    the age group of the animal
     * @param foodType      the type of food product
     * @param careProducts  the type of care products
     * @param toys          the type of toys
     * @param accessories   the type of accessories
     * @param brand         the brand of the products
     * @return a list of ProductDTO objects matching the specified criteria
     */
    public List<ProductDTO> searchProductsByCriteria(String animalTypes, String animalAges, String foodType, String careProducts, String toys, String accessories, String brand) {
        Set<Product> productSet = new HashSet<>();

        // Adaugă produsele pe baza tipului de animal
        if (animalTypes != null && !animalTypes.isEmpty()) {
            productSet.addAll(productRepository.findByNameContainingIgnoreCase(animalTypes));
        }

        // Determinarea criteriului de vârstă
        int age;
        try {
            age = Integer.parseInt(animalAges);
        } catch (NumberFormatException e) {
            age = -1; // Vârstă invalidă
        }

        String ageCriteria = "";
        if (age >= 0) {
            ageCriteria = age < 1 ? " pui" : " adult";
        }

        // Combină criteriile ageCriteria, foodType și brand
        if (!ageCriteria.isEmpty() || (foodType != null && !foodType.isEmpty()) || (brand != null && !brand.isEmpty())) {
            String combinedCriteria = (animalTypes != null && !animalTypes.isEmpty() ? animalTypes : "") +
                    ageCriteria +
                    (foodType != null && !foodType.isEmpty() ? " " + foodType : "") +
                    (brand != null && !brand.isEmpty() ? " " + brand : "");
            productSet.addAll(productRepository.findByNameContainingIgnoreCase(combinedCriteria.trim()));
        }

        // Căutare pe baza produselor de îngrijire
        if (careProducts != null && !careProducts.isEmpty()) {
            productSet.addAll(productRepository.findByNameContainingIgnoreCase(animalTypes + " " + careProducts));
        }

        // Căutare pe baza jucăriilor
        if (toys != null && !toys.isEmpty()) {
            productSet.addAll(productRepository.findByNameContainingIgnoreCase(animalTypes + " " + toys));
        }

        // Căutare pe baza accesoriilor
        if (accessories != null && !accessories.isEmpty()) {
            productSet.addAll(productRepository.findByNameContainingIgnoreCase(animalTypes + " " + accessories));
        }

        // Conversia setului de produse în listă de DTO-uri
        List<ProductDTO> combinedProductList = productSet.stream()
                .map(ProductMapper::toProductDTO)
                .collect(Collectors.toList());

        return combinedProductList;
    }
}