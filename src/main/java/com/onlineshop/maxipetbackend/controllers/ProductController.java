package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.ProductDTO;
import com.onlineshop.maxipetbackend.services.ProductService;
import com.onlineshop.maxipetbackend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
@CrossOrigin
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private List<ProductDTO> products;

    /**
     * Retrieves a list of products.
     * @return ModelAndView for displaying products
     */
    @GetMapping("/findAllProducts")
    public ModelAndView getProducts(){
        products = productService.findProducts();
        ModelAndView modelAndView = new ModelAndView("products");
        modelAndView.addObject("products", products);
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }

    /**
     * Retrieves a list of products sorted by price.
     *
     * @param sortOrder The sorting order. Can be "asc" for ascending or "desc" for descending. Optional parameter.
     * @return ModelAndView for displaying sorted products
     */
    @GetMapping("/sortByPrice")
    public ModelAndView getProductsByPrice(@RequestParam(required = false) String sortOrder){
        ModelAndView modelAndView = new ModelAndView("products");
        List<ProductDTO> sortedProducts = productService.sortProductsByPrice(sortOrder);
        modelAndView.addObject("products", sortedProducts);
        return modelAndView;
    }

    /**
     * Inserts a new product into the system.
     *
     * @param name          The name of the product.
     * @param price         The price of the product.
     * @param stock         The stock quantity of the product.
     * @param categoryName  The category name of the product.
     * @param categoryAnimal  The animal category of the product.
     * @param image         The image file of the product.
     * @return ModelAndView for redirecting to the list of all products with success or error message.
     */
    @PostMapping("/insertProduct")
    public ModelAndView insertProduct(@RequestParam("name") String name,
                                      @RequestParam("price") double price,
                                      @RequestParam("stock") int stock,
                                      @RequestParam("categoryName") String categoryName,
                                      @RequestParam("categoryAnimal") String categoryAnimal,
                                      @RequestParam("image") MultipartFile image) {
        ModelAndView mav = new ModelAndView("redirect:/product/findAllProducts");

        try {
            // Salvează imaginea în sistemul de fișiere
            String filename = image.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/" + filename);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());

            // Creează și salvează ProductDTO
            ProductDTO productDTO = ProductDTO.builder()
                    .name(name)
                    .price(price)
                    .stock(stock)
                    .categoryName(categoryName)
                    .categoryAnimal(categoryAnimal)
                    .imageUrl("/static/" + filename)
                    .build();

            String productId = productService.insert(productDTO);
            if (productId != null) {
                mav.addObject("successInsert", "Inserarea produsului a avut loc cu succes.");
            } else {
                mav.addObject("errorInsert", "Inserarea produsului a eșuat. Vă rugăm să încercați din nou.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("errorInsert", "A apărut o eroare. Vă rugăm să încercați din nou.");
        }

        return mav;
    }

    /**
     * Returns the product with the specified ID.
     * @param productId The ID of the product to be retrieved
     */
    @GetMapping("findById/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") String productId){
        ProductDTO productDTO = productService.findProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    /**
     * Updates a product.
     * @param productId The ID of the product to be updated
     * @param productDTO The object containing the new information for the product to be updated
     * @param image The image file of the product
     * @return A ModelAndView object representing the updated product
     */
    @PostMapping ("/updateProduct/{id}")
    public ModelAndView updateProductP(@PathVariable("id") String productId, ProductDTO productDTO,  @RequestParam("image") MultipartFile image){
        return updateProduct(productId, productDTO, image);
    }

    /**
     * Updates a product.
     * @param productId The ID of the product to be updated
     * @param productDTO The object containing the new information for the product to be updated
     * @param image The image file of the product
     * @return A ModelAndView object representing the updated product
     */
    @PutMapping("/updateP/{id}")
    public ModelAndView updateProduct(@PathVariable("id") String productId, ProductDTO productDTO,  @RequestParam("image") MultipartFile image){
        ModelAndView mav = new ModelAndView();
        ProductDTO product = productService.update(productId, productDTO, image);
        if(product != null){
            mav.addObject("successUpdate", "Update produsului a avut loc cu suces.");
        }else {
            mav.addObject("errorUpdate", "Produsul nu a fost gasit in baza de date");
        }
        mav.setViewName("redirect:/product/findAllProducts");
        return mav;
    }

    /**
     * Deletes a product.
     * @param productId The ID of the product to be deleted
     * @return A ModelAndView object representing the deletion status
     */
    @PostMapping ("/removeProduct/{id}")
    public ModelAndView removeProduct(@PathVariable("id") String productId){
        return  deleteProduct(productId);
    }

    /**
     * Deletes a product.
     * @param productId The ID of the product to be deleted
     * @return A ModelAndView object representing the deletion status
     */
    @DeleteMapping("/deleteProduct/{id}")
    public ModelAndView deleteProduct(@PathVariable("id") String productId){
        ModelAndView mav = new ModelAndView();
        int rez = productService.delete(productId);
        if(rez == 0){
            mav.addObject("successDelete", "Stergerea produsului a avut loc cu suces.");
        }else{
            mav.addObject("errorDelete", "Produsul nu a fost gasit in baza de date");
        }
        mav.setViewName("redirect:/product/findAllProducts");
        return mav;
    }
}