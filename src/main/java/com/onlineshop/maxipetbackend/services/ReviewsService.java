package com.onlineshop.maxipetbackend.services;


import com.onlineshop.maxipetbackend.constants.ReviewsLogger;
import com.onlineshop.maxipetbackend.dtos.ReviewsDTO;
import com.onlineshop.maxipetbackend.dtos.mappers.ReviewsMapper;
import com.onlineshop.maxipetbackend.entities.LocalUser;
import com.onlineshop.maxipetbackend.entities.Product;
import com.onlineshop.maxipetbackend.entities.Reviews;
import com.onlineshop.maxipetbackend.repositories.ProductRepository;
import com.onlineshop.maxipetbackend.repositories.ReviewsRepository;
import com.onlineshop.maxipetbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewsService {
    public static final Logger LOGGER = LoggerFactory.getLogger(PromotionsService.class);
    private final ReviewsRepository reviewsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Finds all reviews.
     *
     * @return The list of reviews
     */
    public List<ReviewsDTO> findReviews(){
        List<Reviews> reviewsList = reviewsRepository.findAll();
        return reviewsList.stream()
                .map(ReviewsMapper::toReviewsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a review by ID.
     *
     * @param id The ID of the review being searched
     * @return The DTO of the found review
     */
    public ReviewsDTO findReviewById(String id){
        Optional<Reviews> optionalReviews = reviewsRepository.findById(id);
        if(!optionalReviews.isPresent()){
            LOGGER.error(ReviewsLogger.REVIEW_WITH_ID_NOT_FOUND, id);
        }
        return ReviewsMapper.toReviewsDTO(optionalReviews.get());
    }

    /**
     * Inserts a new review.
     *
     * @param reviewsDTO The DTO of the review to be inserted
     * @return The ID of the inserted review
     */
    public String insertReview(ReviewsDTO reviewsDTO){
        Reviews  reviews = ReviewsMapper.toEntity(reviewsDTO);
        Optional<Product> product = productRepository.findById(reviewsDTO.getProductId());
        Optional<LocalUser> user = userRepository.findById(reviewsDTO.getUserId());
        reviews.setProduct(product.get());
        reviews.setLocalUser(user.get());
        reviews = reviewsRepository.save(reviews);
        LOGGER.debug(ReviewsLogger.REVIEW_WITH_ID_INSERTED, reviews.getId());
        return reviews.getId();
    }

    /**
     * Deletes a review.
     *
     * @param id The ID of the review to be deleted
     */
    public void delete(String id){
        Optional<Reviews> optionalReviews = reviewsRepository.findById(id);
        if(!optionalReviews.isPresent()){
            LOGGER.error(ReviewsLogger.REVIEW_WITH_ID_NOT_FOUND, id);
        }else {
            reviewsRepository.deleteById(id);
            LOGGER.debug(ReviewsLogger.REVIEW_WITH_ID_DELETED, id);
        }
    }

    /**
     * Updates a review.
     *
     * @param id          The ID of the review to be updated
     * @param reviewsDTO  The DTO containing the new information for the review to be updated
     * @return The DTO of the updated review
     */
    public ReviewsDTO update(String id, ReviewsDTO reviewsDTO) {
        Optional<Reviews> optionalReviews = reviewsRepository.findById(id);
        if (optionalReviews.isPresent()) {
            Reviews existingReview = optionalReviews.get();
            if (reviewsDTO.getRating() != 0) {
                existingReview.setRating(reviewsDTO.getRating());
            }
            if (reviewsDTO.getComment() != null) {
                existingReview.setComment(reviewsDTO.getComment());
            }
            existingReview = reviewsRepository.save(existingReview);
            LOGGER.debug(ReviewsLogger.REVIEW_WITH_ID_UPDATED, existingReview.getId());
            return ReviewsMapper.toReviewsDTO(existingReview);
        } else {
            LOGGER.error(ReviewsLogger.REVIEW_WITH_ID_NOT_FOUND, id);
            return null;
        }
    }

    /**
     * Finds all reviews for a user.
     *
     * @param userId The ID of the user
     * @return The list of reviews placed by the user
     */
    public List<ReviewsDTO> findAllReviewsForUser(String userId){
        List<Reviews> reviews = reviewsRepository.findByLocalUser_Id(userId);
        if(reviews.isEmpty()){
            LOGGER.error("The user with the id {} has not placed any reviews", userId);
        }else{
            LOGGER.info("The user with the id {} has placed reviews", userId);
        }
        return reviews.stream()
                .map(ReviewsMapper::toReviewsDTO)
                .collect(Collectors.toList());
    }

}
