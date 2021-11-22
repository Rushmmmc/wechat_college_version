package com.mywechat.util;

import com.mywechat.model.Constant;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 功能：生成验证码
 */
public class VerifyCode {
    /**
     * 设置验证码的长，宽
     */
    private int width = 120;
    private int height = 39;
    private Random random = new Random();
    private Color backgroundColor = new Color(255, 255, 255);
    private String code;

    /**
     * 获取随机颜色
     *
     * @return
     */
    private Color randomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return new Color(red, green, blue);
    }

    /**
     * 在验证码图片中增加干扰线条
     *
     * @param image
     */
    private void drawLine(BufferedImage image) {
        int num = 5;
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            graphics.setStroke(new BasicStroke(1.5F));
            graphics.setColor(Color.BLUE);
            graphics.drawLine(x1, y1, x2, y2);
        }
    }

    private BufferedImage createImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(this.backgroundColor);
        g2.fillRect(0, 0, width, height);
        return image;
    }

    public BufferedImage getImage() {
        BufferedImage image = createImage();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        StringBuilder stringBuilder = new StringBuilder();
        // 向图片中画4个字符
        for (int i = 0; i < Constant.CODESIZE; i++) {
            String rand = String.valueOf(random.nextInt(10));
            stringBuilder.append(rand);
            float x = i * 1.0F * width / 4;
            graphics.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            graphics.setColor(randomColor());
            graphics.drawString(rand, x, height / 2);
        }
        this.code = stringBuilder.toString();
        drawLine(image);
        return image;
    }

    public String getText() {
        return code;
    }

    public static void output(BufferedImage image, OutputStream out)
            throws IOException {
        ImageIO.write(image, "JPEG", out);
    }

}
