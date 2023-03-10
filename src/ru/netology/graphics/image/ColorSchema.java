package ru.netology.graphics.image;

public class ColorSchema implements TextColorSchema {
    private char[] colorChars = {'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};
    @Override
    public char convert(int color) {
        return colorChars[color / (261 / colorChars.length )];
    }
}
