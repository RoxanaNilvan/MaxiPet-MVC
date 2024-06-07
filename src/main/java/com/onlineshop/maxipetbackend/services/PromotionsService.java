package com.onlineshop.maxipetbackend.services;


import com.onlineshop.maxipetbackend.constants.PromotionsLogger;
import com.onlineshop.maxipetbackend.dtos.PromotionsDTO;
import com.onlineshop.maxipetbackend.dtos.mappers.PromotionsMapper;
import com.onlineshop.maxipetbackend.entities.Product;
import com.onlineshop.maxipetbackend.entities.Promotions;
import com.onlineshop.maxipetbackend.repositories.ProductRepository;
import com.onlineshop.maxipetbackend.repositories.PromotionsRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PromotionsService {
    public static final Logger LOGGER = LoggerFactory.getLogger(PromotionsService.class);
    private final PromotionsRepository promotionsRepository;
    private final ProductRepository productRepository;

    /**
     * Returns a list of promotions.
     *
     * @return The list of DTO objects representing promotions
     */
    public List<PromotionsDTO> findPromotions(){
        List<Promotions> categoryList = promotionsRepository.findAll();
        return categoryList.stream()
                .map(PromotionsMapper::toPromotionsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns the promotion with the specified ID.
     *
     * @param id The ID of the promotion being searched
     * @return The DTO of the found promotion
     */
    public PromotionsDTO findById(String id){
        Optional<Promotions> optionalPromotions = promotionsRepository.findById(id);
        if(!optionalPromotions.isPresent()){
            LOGGER.error(PromotionsLogger.PROMOTION_WITH_ID_NOT_FOUND, id);
        }
        return PromotionsMapper.toPromotionsDTO(optionalPromotions.get());
    }

    /**
     * Inserts a new promotion.
     *
     * @param promotionsDTO The DTO of the promotion to be inserted
     * @return The ID of the inserted promotion
     */
    public String insertPromotion(PromotionsDTO promotionsDTO){
        if (promotionsDTO == null || promotionsDTO.getProductName() == null) {
            LOGGER.error("PromotionsDTO or productName is null.");
            return null;
        }

        Product product = productRepository.findByName(promotionsDTO.getProductName());
        if (product == null) {
            LOGGER.error("Product with name {} not found.", promotionsDTO.getProductName());
            return null;
        }
        Promotions promotions = PromotionsMapper.toEntity(promotionsDTO);
        promotions.setInitialPrice(product.getPrice());
        double finalPrice = product.getPrice() - (promotions.getDiscount() * product.getPrice()/100);
        promotions.setDiscountedPrice(finalPrice);
        if(promotions.getDiscountDate().isAfter(LocalDate.now())){
            product.setPrice(finalPrice);
        } else {
            product.setPrice(promotions.getInitialPrice());
        }
        promotions = promotionsRepository.save(promotions);
        product.setPromotions(promotions);
        productRepository.save(product);
        LOGGER.debug("Promotion with ID {} inserted.", promotions.getId());
        return promotions.getId();
    }


    /**
     * Deletes a promotion.
     *
     * @param id The ID of the promotion to be deleted
     */
    public int delete(String id){
        System.err.println(id);
        Optional<Promotions> optionalPromotions = promotionsRepository.findById(id);
        if(!optionalPromotions.isPresent()){
            LOGGER.error(PromotionsLogger.PROMOTION_WITH_ID_NOT_FOUND, id);
            return -1;
        }else {
            Product product = productRepository.findProductByPromotionsId(id);
            product.setPromotions(null);
            product.setPrice(optionalPromotions.get().getInitialPrice());
            productRepository.save(product);
            promotionsRepository.deleteById(id);
            LOGGER.debug(PromotionsLogger.PROMOTION_WITH_ID_DELETED, id);
            return 0;
        }
    }

    /**
     * Updates a promotion.
     *
     * @param id The ID of the promotion to be updated
     * @param promotionsDTO The DTO containing the new information for the promotion to be updated
     * @return The DTO of the updated promotion
     */
    public PromotionsDTO update(String id, PromotionsDTO promotionsDTO) {
        Optional<Promotions> optionalPromotions = promotionsRepository.findById(id);
        if (optionalPromotions.isPresent()) {
            Promotions existingPromotions = optionalPromotions.get();

            if (promotionsDTO.getDiscount() > 0) {
                existingPromotions.setDiscount(promotionsDTO.getDiscount());
            }

            existingPromotions = promotionsRepository.save(existingPromotions);
            LOGGER.debug(PromotionsLogger.PROMOTION_WITH_ID_UPDATED, existingPromotions.getId());
            return PromotionsMapper.toPromotionsDTO(existingPromotions);
        } else {
            LOGGER.error(PromotionsLogger.PROMOTION_WITH_ID_NOT_FOUND, id);
            return null;
        }
    }
}
