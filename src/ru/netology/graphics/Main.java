package ru.netology.graphics;

import ru.netology.graphics.image.ColorSchema;
import ru.netology.graphics.image.Converter;
import ru.netology.graphics.image.TextGraphicsConverter;
import ru.netology.graphics.server.GServer;

import java.io.File;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new Converter(); // Создайте тут объект вашего класса конвертера
        GServer server = new GServer(converter); // Создаём объект сервера
        server.start(); // Запускаем


//        TextGraphicsConverter converter = new Converter();
//
//        converter.setMaxRatio(4);
//        converter.setMaxWidth(300);
//        converter.setMaxHeight(300);
//
//        // Или то же, но с выводом на экран:
//        String url = "https://i.ibb.co/MGKNh34/test.png";
//        String imgTxt = converter.convert(url);
//        System.out.println(imgTxt);
    }
}
