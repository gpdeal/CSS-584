/*
 * Project 1
 */
package assignment.pkg1;

import java.awt.image.BufferedImage;
import java.lang.Object.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

import java.awt.Color;

public class readImage {

    int imageCount = 1;
    int intensityBins[] = new int[26];
    int intensityMatrix[][] = new int[100][26];
    int colorCodeBins[] = new int[65];
    int colorCodeMatrix[][] = new int[100][65];

    /*Each image is retrieved from the file.  The height and width are found for the image and the getIntensity and
   * getColorCode methods are called.
     */
    public readImage() {
        int successfulReads = 0;
        int failedReads = 0;
        while (imageCount < 101) {

            try {

                // Read the next image file
                File file;
                // src\\assignment\\pkg1\\
                file = new File("images/" + imageCount + ".jpg");
                BufferedImage image = ImageIO.read(file);

                //successfulReads++;
                
                int height = image.getHeight();
                int width = image.getWidth();
                
                getIntensity(image, height, width);
                getColorCode(image, height, width);
            } catch (Exception e) {
                System.out.println("Error occurred when reading or processing image " + imageCount + ".");
                //failedReads++;
            }
            ++imageCount;
        }

        writeIntensity();
        writeColorCode();

    }

    //intensity method 
    public void getIntensity(BufferedImage image, int height, int width) {

        // save the total number of pixels in the image into the first bin
        intensityBins[0] = height * width;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // retrieve RGB values of current pixel
                int rgbValues[] = extractRGB(image, x, y);

                // maybe faster, maybe less safe than the Color method above
                //int red = (pixel & 0x00ff0000) >> 16;
                //int green = (pixel & 0x0000ff00) >> 8;
                //int blue = pixel & 0x000000ff;
                
                // calculate intensity value
                int intensity = (int) (
                        (0.299 * rgbValues[0])    // red
                        + (0.587 * rgbValues[1])  // green
                        + (0.114) * rgbValues[2]  // blue
                        );
                
                // increment the bin correspinding to the pixel's I-value
                int bin = Math.min((intensity / 10) + 1, intensityBins.length - 1);
                intensityBins[bin]++;
            }
        }

        // add contents of intensityBins to intensityMatrix and clear the
        // contents of intensityBins
        for (int i = 0; i < intensityBins.length; ++i) {
            intensityMatrix[imageCount - 1][i] = intensityBins[i];
            intensityBins[i] = 0;
        }
    }

    //color code method
    public void getColorCode(BufferedImage image, int height, int width) {
        // save the total number of pixels in the image into the first bin
        colorCodeBins[0] = height * width;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // retrieve RGB values of current pixel
                int rgbValues[] = extractRGB(image, x, y);
                
                // extract the two most significant bits from each of the RGB
                // values
                int colorCodeBits[] = new int[6];
                colorCodeBits[0] = rgbValues[0] >> 7 & 1;
                colorCodeBits[1] = rgbValues[0] >> 6 & 1;
                colorCodeBits[2] = rgbValues[1] >> 7 & 1;
                colorCodeBits[3] = rgbValues[1] >> 6 & 1;
                colorCodeBits[4] = rgbValues[2] >> 7 & 1;
                colorCodeBits[5] = rgbValues[2] >> 6 & 1;
                
                // construct color code
                byte colorCode = 0;
                for (int i = 0; i < 6; i++) {
                    colorCode <<= 1;
                    colorCode |= colorCodeBits[i];
                }
                
                colorCodeBins[colorCode + 1]++;
            }
        }
        
        // add contents of colorCodeBins to colorCodeMatrix and clear the
        // contents of colorCodeBins
        for (int i = 0; i < colorCodeBins.length; ++i) {
            colorCodeMatrix[imageCount - 1][i] = colorCodeBins[i];
            colorCodeBins[i] = 0;
        }
        
    }

    
    private int[] extractRGB(BufferedImage image, int x, int y) {
        int rgbValues[] = new int[3];
        
        int pixel = image.getRGB(x, y);
        Color color = new Color(pixel);
        rgbValues[0] = color.getRed();
        rgbValues[1] = color.getGreen();
        rgbValues[2] = color.getBlue();
        
        return rgbValues;
    }
    
    
    //This method writes the contents of the colorCode matrix to a file named colorCode.txt.
    public void writeColorCode() {
        writeMatrixFile(colorCodeMatrix, "colorCode.txt");
    }

    //This method writes the contents of the intensity matrix to a file called intensity.txt
    public void writeIntensity() {
        writeMatrixFile(intensityMatrix, "intensity.txt");
    }

    
    private void writeMatrixFile(int matrix[][], String filename) {
        try {
            ObjectOutputStream outputStream;
            outputStream = new ObjectOutputStream(new FileOutputStream(filename));
            outputStream.writeObject(matrix);
        } catch (Exception e) {
            System.out.println("Error occurred when writing to file " + filename);
        }
    }
    
    
    public static void main(String[] args) {
        new readImage();
    }

}
