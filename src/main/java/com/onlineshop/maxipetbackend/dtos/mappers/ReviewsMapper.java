package com.onlineshop.maxipetbackend.dtos.mappers;

import com.onlineshop.maxipetbackend.dtos.ReviewsDTO;
import com.onlineshop.maxipetbackend.entities.Reviews;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReviewsMapper {
    public static ReviewsDTO toReviewsDTO(Reviews reviews){
        return ReviewsDTO.builder()
                .id(reviews.getId())
                .rating(reviews.getRating())
                .comment(reviews.getComment())
                .productId(reviews.getProduct().getId())
                .userId(reviews.getLocalUser().getId())
                .productName(reviews.getProduct().getName())
                .userLastName(reviews.getLocalUser().getLastName())
                .userFirstName(reviews.getLocalUser().getFirstName())
                .build();
    }

    public static Reviews toEntity(ReviewsDTO reviewsDTO){
        return Reviews.builder()
                .id(reviewsDTO.getId())
                .rating(reviewsDTO.getRating())
                .comment(reviewsDTO.getComment())
                .build();
    }
}
