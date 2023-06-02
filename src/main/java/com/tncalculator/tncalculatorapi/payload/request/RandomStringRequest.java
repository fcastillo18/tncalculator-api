package com.tncalculator.tncalculatorapi.payload.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RandomStringRequest {
    private String userId;
    private String randomString;
}
