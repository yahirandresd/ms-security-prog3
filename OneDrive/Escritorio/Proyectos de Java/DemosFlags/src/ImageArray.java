/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Cesar Franco
 */
public class ImageArray {

    public static boolean saveImage(int[][] imageArray, String path) {
        boolean saved = true;
        BufferedImage image = new BufferedImage(imageArray[0].length, imageArray.length, BufferedImage.TYPE_INT_ARGB);
        File file = null;

        for (int r = 0; r < imageArray.length; r++) {
            for (int c = 0; c < imageArray[0].length; c++) {
                image.setRGB(c, r, imageArray[r][c]);
            }
        }

        file = new File(path);

        String fileType = path.substring(path.lastIndexOf(".")+1);
        try {
            ImageIO.write(image, fileType, file);
        }
        catch (IOException ex) {
            Logger.getLogger(ImageArray.class.getName()).log(Level.SEVERE, null, ex);
            saved = false;
        }

        return saved;
    }
    
    public static boolean saveImage(int[][] redImage, int[][] greenImage, int[][] blueImage, int[][] alphaImage, String path) {
        boolean saved = true;
        BufferedImage image = new BufferedImage(redImage[0].length, redImage.length, BufferedImage.TYPE_INT_ARGB);
        File file = null;
        Color color = null;
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 0;

        for (int r = 0; r < redImage.length; r++) {
            for (int c = 0; c < redImage[0].length; c++) {
                red = redImage[r][c];
                green = greenImage[r][c];
                blue = blueImage[r][c];
                alpha = alphaImage[r][c];
                color = new Color(red, green, blue, alpha);
                image.setRGB(c, r, color.getRGB());
            }
        }

        file = new File(path);

        String fileType = path.substring(path.lastIndexOf(".")+1);
        try {
            ImageIO.write(image, fileType, file);
        }
        catch (IOException ex) {
            Logger.getLogger(ImageArray.class.getName()).log(Level.SEVERE, null, ex);
            saved = false;
        }

        return saved;
    }

    public static int[][] loadImage(String path) {
        BufferedImage image = null;
        int[][] imageArray = {};

        try {
            image = ImageIO.read(new File(path));
        }
        catch (IOException e) {

        }
        imageArray = new int[image.getHeight()][image.getWidth()];

        for (int r = 0; r < imageArray.length; r++) {
            for (int c = 0; c < imageArray[0].length; c++) {
                imageArray[r][c] = image.getRGB(c, r);
            }
        }

        return imageArray;
    } 
    
    public static int[][] getRedChannel(int[][] imageArrayOri){
        int height = imageArrayOri.length;
        int width = imageArrayOri[0].length;
        int[][] imageArrayDes = new int[height][width];
        Color color = null;
        
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                color = new Color(imageArrayOri[r][c]);
                imageArrayDes[r][c] = color.getRed();
            }
        }
        
        return imageArrayDes;
        
    }
    
    public static int[][] getGreenChannel(int[][] imageArrayOri){
        int height = imageArrayOri.length;
        int width = imageArrayOri[0].length;
        int[][] imageArrayDes = new int[height][width];
        Color color = null;
        
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                color = new Color(imageArrayOri[r][c]);
                imageArrayDes[r][c] = color.getGreen();
            }
        }
        
        return imageArrayDes;
        
    }
    
    public static int[][] getBlueChannel(int[][] imageArrayOri){
        int height = imageArrayOri.length;
        int width = imageArrayOri[0].length;
        int[][] imageArrayDes = new int[height][width];
        Color color = null;
        
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                color = new Color(imageArrayOri[r][c]);
                imageArrayDes[r][c] = color.getBlue();
            }
        }
        
        return imageArrayDes;
        
    }
    
    public static int[][] getAlphaChannel(int[][] imageArrayOri){
        int height = imageArrayOri.length;
        int width = imageArrayOri[0].length;
        int[][] imageArrayDes = new int[height][width];
        Color color = null;
        
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                color = new Color(imageArrayOri[r][c]);
                imageArrayDes[r][c] = color.getAlpha();
            }
        }
        
        return imageArrayDes;
        
    }
    
    public static int[][] createFillMatrix(int[][] imageArrayOri, int value){
        int height = imageArrayOri.length;
        int width = imageArrayOri[0].length;
        int[][] imageArrayDes = new int[height][width];
        
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                imageArrayDes[r][c] = value;
            }
        }
        
        return imageArrayDes;
    }
    
    public static int[][] combineRGBAChannels(int[][] redImg, int[][] greenImg, int[][] blueImg, int[][] alphaImg){
        int height = redImg.length;
        int width = redImg[0].length;
        int[][] imageArrayDes = new int[height][width];
        Color color = null;
        
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                color = new Color(redImg[r][c], greenImg[r][c], blueImg[r][c], alphaImg[r][c]);
                imageArrayDes[r][c] = color.getRGB();
            }
        }
        
        return imageArrayDes;
    }
}
