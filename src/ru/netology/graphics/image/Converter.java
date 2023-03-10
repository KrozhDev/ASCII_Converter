package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    private int maxWidth;
    private int maxHeight;
    private boolean ifChangeWidth;
    private boolean ifChangeHeight;
    private boolean ifChangeRatio;
    private double maxRatio;
    private double coef;
    private TextColorSchema schema;

    public Converter() {
        ifChangeRatio = false;
        ifChangeHeight = false;
        ifChangeWidth = false;
        coef = 1;
        schema = new ColorSchema();
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        //скачаем картинку из интернета
        BufferedImage img = ImageIO.read(new URL(url));

        // Если конвертер попросили проверять на максимально допустимое
        // соотношение сторон изображения, то вам здесь надо сделать эту проверку,
        // и, если картинка не подходит, выбросить исключение BadImageSizeException.
        if (ifChangeRatio) {
            double ratio = (double) img.getWidth()/ img.getHeight();
            if ( ratio > maxRatio) {
                throw new BadImageSizeException(maxRatio, ratio);
            }
        }

        // Чтобы получить ширину картинки, вызовите img.getWidth(), высоту - img.getHeight()
        // Если конвертеру выставили максимально допустимые ширину и/или высоту,
        // вам надо по ним и по текущим высоте и ширине вычислить новые высоту и ширину.

        if (ifChangeHeight | ifChangeWidth) {
            if (img.getWidth() > maxWidth | img.getHeight() > maxHeight) {
                if (img.getWidth() - maxWidth > img.getHeight() - maxHeight) {
                    coef = (double) maxWidth / img.getWidth();
                } else {
                    coef = (double) maxHeight / img.getHeight();
                }
            }
        }

        int newWidth = (int) Math.floor(coef * img.getWidth());
        int newHeight = (int) Math.floor(coef * img.getWidth());

        // Теперь нам надо попросить картинку изменить свои размеры на новые.
        // Последний параметр означает, что мы просим картинку плавно сузиться
        // на новые размеры. В результате мы получаем ссылку на новую картинку, которая
        // представляет собой суженную старую.
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        //проверяем этап сужения
        //ImageIO.write(scaledImage, "png", new File("scaledImage.png"));

        // Теперь сделаем её чёрно-белой. Для этого поступим так:
        // Создадим новую пустую картинку нужных размеров, заранее указав последним
        // параметром чёрно-белую цветовую палитру:
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Попросим у этой картинки инструмент для рисования на ней:
        Graphics2D graphics = bwImg.createGraphics();

        // А этому инструменту скажем, чтобы он скопировал содержимое из нашей суженной картинки:
        graphics.drawImage(scaledImage, 0, 0, null);

        // Теперь в bwImg у нас лежит чёрно-белая картинка нужных нам размеров.
        //проверяем этап обесцвечивания
        ImageIO.write(bwImg, "png", new File("bwImage.png"));

        //для прохода по пикселям нам нужен будет этот инструмент:
        //мы можем спросить пиксель на нужных нам координатах, указав номер столбца (w) и строки (h)
        //int color = bwRaster.getPixel(w, h, new int[3])[0];
        WritableRaster bwRaster = bwImg.getRaster();

        // пробегаемся по массиву и меняем  пиксели на символы

        int[] colorArr = new int[3];
        StringBuilder chars = new StringBuilder();
        ColorSchema schema = new ColorSchema();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, colorArr)[0];
                char c = schema.convert(color);
                for (int i = 0; i < 1; i++) {
                    chars.append(c);
                    chars.append(" ");
                }
            }
            chars.append("\n");
        }
        return chars.toString(); // Возвращаем собранный текст.
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
        this.ifChangeWidth = true;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
        this.ifChangeHeight = true;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
        this.ifChangeRatio = true;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
