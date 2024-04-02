package com.qnit18.springtutorial.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Giúp tao ra builder class tien hon
@FieldDefaults(level = AccessLevel.PRIVATE) // Moi field trong nay deu la private
public class IntrospectTokenRequest {
    String token;
}

