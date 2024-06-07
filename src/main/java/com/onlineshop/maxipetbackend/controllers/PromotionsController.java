package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.PromotionsLogger;
import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.PromotionsDTO;
import com.onlineshop.maxipetbackend.services.PromotionsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
@CrossOrigin
@RequestMapping("/promotions")
@AllArgsConstructor
public class PromotionsController {
    private final PromotionsService promotionsService;

    /**
     * Retrieves a list of promotions.
     *
     * @return A ModelAndView object representing the view of promotions
     */
    @GetMapping("/findAllPromotions")
    public ModelAndView getPromotions(){
        List<PromotionsDTO> promotions= promotionsService.findPromotions();
        ModelAndView modelAndView = new ModelAndView("promotions");
        modelAndView.addObject("promotions", promotions);
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }

    /**
     * Retrieves the promotion with the specified ID.
     *
     * @param promotionId The ID of the promotion to retrieve
     * @return A ResponseEntity with the DTO object of the promotion and HttpStatus.OK if the promotion is found
     */
    @GetMapping("/findById/{id}")
    public ResponseEntity<PromotionsDTO> findPromotion(@PathVariable("id") String promotionId){
        PromotionsDTO promotionsDTO = promotionsService.findById(promotionId);
        return new ResponseEntity<>(promotionsDTO, HttpStatus.OK);
    }

    /**
     * Inserts a new promotion.
     *
     * @param promotionsDTO The object representing the promotion to insert
     * @return The ID of the inserted promotion
     */
    @PostMapping("/insertPromotion")
    public ModelAndView insetPromotion(@Valid PromotionsDTO promotionsDTO){
        String promotionId = promotionsService.insertPromotion(promotionsDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/promotions/findAllPromotions");
        System.err.println(promotionId);
        if(promotionId == null){
            mav.addObject("errorInsertPromotion", PromotionsLogger.PROMOTION_INSERT_FAILED);
        }else{
            mav.addObject("successInsertPromotion", PromotionsLogger.PROMOTION_INSERT_SUCCESS);
        }
        return mav;
    }

    /**
     * Deletes a promotion.
     *
     * @param promotionId The ID of the promotion to delete
     */
    @PostMapping("/deletePromotion/{id}")
    public ModelAndView deletePromotion(@PathVariable("id") String promotionId){
        System.out.println(promotionId);
        return deletePromotions(promotionId);
    }

    /**
     * Deletes a promotion.
     *
     * @param promotionId The ID of the promotion to delete
     */
    @DeleteMapping("/deleteP/{id}")
    public ModelAndView deletePromotions(@PathVariable("id") String promotionId){
        int rez = promotionsService.delete(promotionId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/promotions/findAllPromotions");
        if(rez == 0){
            mav.addObject("successDeletePromotion", PromotionsLogger.PROMOTION_DELETE_SUCCESS);
        }else{
            mav.addObject("errorDeletePromotion", PromotionsLogger.PROMOTION_DELETE_FAILED);
        }
        return mav;
    }

    /**
     * Updates a promotion.
     *
     * @param promotionId The ID of the promotion to update
     * @param promotionsDTO The object containing the new information for the promotion to update
     * @return The object representing the updated promotion
     */
    @PostMapping("/updatePromotion/{id}")
    public ModelAndView updatePromotionH(@PathVariable("id") String promotionId, PromotionsDTO promotionsDTO){
        return updatePromotion(promotionId, promotionsDTO);
    }

    /**
     * Updates a promotion.
     *
     * @param promotionId The ID of the promotion to update
     * @param promotionsDTO The object containing the new information for the promotion to update
     * @return The object representing the updated promotion
     */
    @PutMapping("/updateP/{id}")
    public ModelAndView updatePromotion(@PathVariable("id") String promotionId, PromotionsDTO promotionsDTO) {
        PromotionsDTO updatedPromotion = promotionsService.update(promotionId, promotionsDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/promotions/findAllPromotions");
        if(updatedPromotion == null){
            mav.addObject("errorUpdatePromotion", PromotionsLogger.PROMOTION_UPDATE_FAILED);
        }else{
            mav.addObject("successUpdatePromotion", PromotionsLogger.PROMOTION_UPDATE_SUCCESS);
        }
        return mav;
    }
}
