package com.onlineshop.maxipetbackend.dtos;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDTO {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
}
