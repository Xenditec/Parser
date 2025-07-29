package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

public class PhoneNumberExtractor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите путь к файлу:");
        String filename = scanner.nextLine();

        Set<String> phoneNumbers = extractPhoneNumbers(filename);

        if (phoneNumbers.isEmpty()) {
            System.out.println("Телефонные номера не найдены");
        } else {
            System.out.println("Найденные телефонные номера:");
            for (String number : phoneNumbers) {
                System.out.println(number);
            }
        }

        scanner.close();
    }

    public static Set<String> extractPhoneNumbers(String filename) {
        Set<String> phoneNumbers = new LinkedHashSet<>(); // Для сохранения порядка и уникальности
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));

            /*
            Несохраняющая группа (?:...) с вариантами:
                \\+7 - литерал "+7" (\\+ экранирует плюс)
                | - ИЛИ
                8 - цифра 8
                | - ИЛИ
                7 - цифра 7

               [\s-]+ - Один или несколько символов из набора [\s-]:
               \s - любой пробельный символ (пробел, таб или другой).
               - собственно дефис.

               \\d{3} - кол-во цифр.
             */

            // Регулярное выражение
            Pattern pattern = Pattern.compile("(?:\\+7|8|7)[\s-]+(\\(?\\d{3}\\)?)[\s-]+\\d{3}[\\s-]+\\d{2}[\\s-]+\\d{2}");

            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String originalNumber = matcher.group().trim();
                phoneNumbers.add(originalNumber);
            }

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return phoneNumbers;
    }
}