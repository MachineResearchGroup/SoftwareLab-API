package com.swl.populator.util;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

@Data
@AllArgsConstructor
public class FakerUtil {

    private static FakerUtil instance;
    public Faker faker;
    public FakeValuesService fakeValuesService;

    public static synchronized FakerUtil getInstance() {
        if (Objects.isNull(instance)) {
            Random random = new Random();
            return new FakerUtil(new Faker(random), new FakeValuesService(new Locale("en"), new RandomService()));
        }
        return instance;
    }
}
