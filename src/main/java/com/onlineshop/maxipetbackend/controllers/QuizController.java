package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.dtos.ProductDTO;
import com.onlineshop.maxipetbackend.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@CrossOrigin
@AllArgsConstructor
public class QuizController {
    private final ProductService productService;

    /**
     * Displays the animal quiz page.
     *
     * @return The name of the view representing the quiz page
     */
    @GetMapping("/animalQuiz")
    public String showQuizPage() {
        return "quiz";
    }

    /**
     * Displays the selected products based on the quiz criteria.
     *
     * @param animalTypes   The selected types of animals
     * @param animalAges    The selected ages of animals
     * @param foodType      The selected type of food
     * @param careProducts  The selected care products
     * @param toys          The selected toys
     * @param accessories   The selected accessories
     * @param brand         The selected brand
     * @return A ModelAndView object representing the view of selected products
     */
    @GetMapping("/findByQuiz")
    public ModelAndView showQuizPageByQuiz(@RequestParam("animalTypes") String animalTypes,
                                           @RequestParam("animalAges") String animalAges,
                                           @RequestParam("foodType") String foodType,
                                           @RequestParam("careProducts") String careProducts,
                                           @RequestParam("toys") String toys,
                                           @RequestParam("accessories") String accessories,
                                           @RequestParam("brand") String brand) {
        System.out.println("animalTypes: " + animalTypes);
        ModelAndView modelAndView = new ModelAndView("productsSelected");
        List<ProductDTO> prducts = productService.searchProductsByCriteria(animalTypes, animalAges, foodType, careProducts, toys, accessories, brand);
        modelAndView.addObject("products", prducts);
        for(ProductDTO product : prducts) {
            System.out.println(product);
        }
        return modelAndView;
    }
}
