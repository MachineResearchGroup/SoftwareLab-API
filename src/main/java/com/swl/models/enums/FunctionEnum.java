package com.swl.models.enums;

public enum FunctionEnum {
  ROLE_USUARIO,
  ROLE_PMO,
  ROLE_PO,
  ROLE_CLIENTE,
  ROLE_DEV;


  public static boolean exists(String function){
    return function.equals(ROLE_DEV.name()) || function.equals(ROLE_PO.name()) || function.equals(ROLE_PMO.name());
  }
}
