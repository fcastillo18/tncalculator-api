package com.tncalculator.tncalculatorapi.payload.response;

import com.tncalculator.tncalculatorapi.constant.Constant;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
  private String token;
  private String type = Constant.JWT_TYPE;
  private Long id;
  private String username;
  private String email;
  private List<String> roles;

  public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }

}