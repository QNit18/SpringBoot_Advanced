package com.qnit18.springtutorial.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PACKAGE)
public class AuthenticationResponseDTO {
    String token;
    boolean authenticated;
}
