package com.swl.exceptions.contract;

import java.util.Map;

public interface MessageException {
    String getExceptionKey();
    Map<String, Object> getMapDetails();
}
