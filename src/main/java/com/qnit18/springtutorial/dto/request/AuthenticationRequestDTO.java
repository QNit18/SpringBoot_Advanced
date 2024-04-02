package com.qnit18.springtutorial.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PACKAGE)
public class AuthenticationRequestDTO {
    String username;
    String password;

}
