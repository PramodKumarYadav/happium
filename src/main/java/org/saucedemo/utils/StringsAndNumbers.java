package org.saucedemo.utils;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@Slf4j
public class StringsAndNumbers {
    public static String getRandomListItem(List<String> list) {
        // Pick a random List item from the list of items
        Integer randomItemNumber = new Random().nextInt(list.size());
        return list.get(randomItemNumber);
    }

    public static String getStringBetween(Locale locale, Integer minimumLength, Integer maximumLength){
        Integer length = getRandomNumber(minimumLength, maximumLength);
        return getStringOfSize(locale, length);
    }

    public static String getStringOfSize(Locale locale, Integer fixedLength){
        // Test Data
        Faker faker = new Faker(locale);

        String generatedString = faker.name().firstName();
        while (generatedString.length() < fixedLength){
            generatedString = String.format("%s %s", generatedString, faker.name().lastName());
        }

        return generatedString.substring(0, Math.min(generatedString.length(), fixedLength));
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
