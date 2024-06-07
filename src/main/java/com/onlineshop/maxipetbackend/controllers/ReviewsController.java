package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.ReviewsDTO;
import com.onlineshop.maxipetbackend.services.ReviewsService;
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
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewsController {
    private final ReviewsService reviewsService;

    /**
     * Retrieves a list of reviews.
     *
     * @return A ModelAndView object representing the view of the reviews
     */
    @GetMapping("/findAllReviews")
    public ModelAndView getReviews() {
        List<ReviewsDTO> reviews = reviewsService.findReviews();
        ModelAndView modelAndView = new ModelAndView("reviews");
        modelAndView.addObject("reviews", reviews);
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }

    /**
     * Retrieves the review with the specified ID.
     *
     * @param reviewsId The ID of the review to be retrieved
     * @return A ResponseEntity object with the DTO of the found review and HttpStatus.OK if the review was found
     */
    @GetMapping("/findById/{id}")
    public ResponseEntity<ReviewsDTO> findReview(@PathVariable("id") String reviewsId) {
        ReviewsDTO reviewsDTO = reviewsService.findReviewById(reviewsId);
        return new ResponseEntity<>(reviewsDTO, HttpStatus.OK);
    }

    /**
     * Inserts a new review submitted by the user.
     *
     * @param reviewsDTO The ReviewsDTO object representing the review to be inserted
     * @return A ModelAndView object redirecting to the customer page with a success or failure message
     */
    @PostMapping("/add")
    public ModelAndView userInsert(@Valid ReviewsDTO reviewsDTO) {
        ModelAndView modelAndView = new ModelAndView("redirect:/customer");
        reviewsDTO.setUserId(UserConstant.userDTO.getId());
        String string = reviewsService.insertReview(reviewsDTO);
        System.out.println(reviewsDTO.getUserId());
        if (string != null) {
            modelAndView.addObject("reviewInserted", "Review inserted successfully");
        }else{
            modelAndView.addObject("reviewFailed", "Review insertion failed");
        }
        return modelAndView;
    }

    /**
     * Inserts a new review.
     *
     * @param reviewsDTO The DTO representing the review to be inserted
     * @return The ID of the inserted review
     */
    @PostMapping("/insert")
    public ModelAndView insetReview(@Valid ReviewsDTO reviewsDTO){
        String reviewId = reviewsService.insertReview(reviewsDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/review/findAllReviews");
        mav.addObject("review", reviewsDTO);
        return mav;
    }

    /**
     * Deletes a review.
     *
     * @param reviewId The ID of the review to be deleted
     */
    @PostMapping ("/removeReview/{id}")
    public ModelAndView removeCategory(@PathVariable("id") String reviewId){
        return deleteReview(reviewId);
    }

    /**
     * Deletes a review.
     *
     * @param reviewId The ID of the review to be deleted
     */
    @DeleteMapping("/deleteReview/{id}")
    public ModelAndView deleteReview(@PathVariable("id") String reviewId){
        reviewsService.delete(reviewId);
        String returning = "Review-ul cu id-ul "+ reviewId + "s-a sters din baza de date";
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/reviews/findAllReviewsByUser/");
        return mav;
    }

    /**
     * Updates a review.
     *
     * @param reviewId         The ID of the review to be updated
     * @param reviewsDTO The DTO containing the new information for the review to be updated
     * @return The DTO of the updated review
     */
    @PostMapping ("/updateReview/{id}")
    public ModelAndView updateReviewR(@PathVariable("id") String reviewId, ReviewsDTO reviewsDTO){
        System.err.println(reviewId);
        return updateReview(reviewId, reviewsDTO);
    }

    /**
     * Updates a review.
     *
     * @param id         The ID of the review to be updated
     * @param reviewsDTO The DTO containing the new information for the review to be updated
     * @return The DTO of the updated review
     */
    @PutMapping("/updateR/{id}")
    public ModelAndView updateReview(@PathVariable String id, ReviewsDTO reviewsDTO) {
        ReviewsDTO updatedReview = reviewsService.update(id, reviewsDTO);
        ModelAndView mav = new ModelAndView();
        if(updatedReview != null){
            mav.addObject("messageUpdateReview", "Review updated successfully");
        }else{
            mav.addObject("messageUpdateReviewFail", "Review update failed");
        }
        mav.setViewName("redirect:/reviews/findAllReviewsByUser/");
        return mav;
    }

    /**
     * Displays all reviews for the current user.
     *
     * @return ModelAndView containing the view name and the list of reviews for the user
     */
    @GetMapping("/findAllReviewsByUser/")
    public ModelAndView findAllReviewsBuyUser() {
        List<ReviewsDTO> reviews = reviewsService.findAllReviewsForUser(UserConstant.userDTO.getId());
        ModelAndView mav = new ModelAndView();
        mav.addObject("reviews", reviews);
        mav.setViewName("reviewsByUser");
        return mav;
    }

}
