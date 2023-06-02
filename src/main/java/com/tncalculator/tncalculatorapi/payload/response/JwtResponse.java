package com.tncalculator.tncalculatorapi.payload.response;

import com.tncalculator.tncalculatorapi.constant.Constant;
import com.tncalculator.tncalculatorapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
  private String token;
  private String type = Constant.JWT_TYPE;
  private User user;
  private List<String> roles;

  public JwtResponse(String accessToken, User user, List<String> roles) {
    this.token = accessToken;
    this.user = user;
    this.roles = roles;
  }

}