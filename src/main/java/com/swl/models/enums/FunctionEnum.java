package com.swl.models.enums;

public enum FunctionEnum {
  ROLE_USER,
  ROLE_PMO,
  ROLE_PO,
  ROLE_CLIENT,
  ROLE_DEV,
  ROLE_ADMIN;


  public static boolean exists(String function){
    return function.equals(ROLE_DEV.name()) || function.equals(ROLE_PO.name()) || function.equals(ROLE_PMO.name());
  }
}
