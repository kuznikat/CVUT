package cz.cvut.fel.pjv;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utility {

    public BufferedImage scaleImg(BufferedImage original, int width, int height) {

        BufferedImage scaledImg = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImg.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImg;
    }
}
