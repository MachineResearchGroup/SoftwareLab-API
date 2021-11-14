package com.swl.util;

import com.swl.models.management.Organization;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
public class CopyUtil {


    private static final List<String> organizationPropsExclude = new ArrayList<>(Arrays.asList("address", "id"));

    public static void copyProperties(Object src, Object trg) {
        List<String> excludeProps = new ArrayList<>(Collections.singletonList("id"));

        if (trg instanceof Organization){
            excludeProps = organizationPropsExclude;
        }

        String[] excludedProperties =
                Arrays.stream(BeanUtils.getPropertyDescriptors(src.getClass()))
                        .map(PropertyDescriptor::getName)
                        .filter(excludeProps::contains)
                        .toArray(String[]::new);

        BeanUtils.copyProperties(src, trg, excludedProperties);
    }
}
