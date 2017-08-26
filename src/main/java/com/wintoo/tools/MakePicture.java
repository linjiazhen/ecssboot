package com.wintoo.tools;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MakePicture  //产生识别验证图像
{
    private static char charTable[] = {
             'A',  'B',  'C',  'D',  'E', 'N',
             'F',  'G',  'H',  'J', 'K', 'L', 'M',
             '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    public static String drawPicture(int width, int height,HttpSession session) {
        if (width <= 0)
            width = 120;
        if (height <= 0)
            height = 65;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(0x00ff33));
//        g.drawRect(0, 0, width, height);
        String str = "";
        for (int x = 0; x < 4; x++) {
            str += charTable[(int) (Math.random() * charTable.length)];
        }
        g.setFont(new Font("Script",Font.BOLD,16));
        g.drawString(str.substring(0, 1), 5, 15);
        g.drawString(str.substring(1, 2), 20, 17);
        g.drawString(str.substring(2, 3), 35, 19);
        g.drawString(str.substring(3, 4), 50, 16);
        Random rand = new Random();
        g.setColor(Color.blue);
//        for (int i = 0; i < 10; i++) {
//            int x = rand.nextInt(width);
//            int y = rand.nextInt(height);
//            g.drawOval(x, y, 1, 1);
//        }
        g.dispose();
        try {
            ImageIO.write(image, "JPEG", new File( "images/check/" + str+".jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return str;
    }
}
