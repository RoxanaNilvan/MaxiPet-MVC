package com.onlineshop.maxipetbackend.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewsDTO {
    private String id;
    private int rating;
    private String comment;
    private String productId;
    private String productName;
    private String userLastName;
    private String userFirstName;
    private String userId;
}
