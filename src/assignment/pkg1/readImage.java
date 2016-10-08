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
    int colorCodeBins[] = new int[64];
    int colorCodeMatrix[][] = new int[100][64];

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
                file = new File("src\\assignment\\pkg1\\images/" + imageCount + ".jpg");
                BufferedImage image = ImageIO.read(file);

                //successfulReads++;
                
                int height = image.getHeight();
                int width = image.getWidth();
                
                getIntensity(image, height, width);
                //getColorCode(image, height, width);
            } catch (Exception e) {
                System.out.println("Error occurred when reading or processing image " + imageCount + ".");
                //failedReads++;
            }
            ++imageCount;
        }

//        // DEBUG
//        System.out.println("Successful reads: " + successfulReads);
//        System.out.println("Failed reads: " + failedReads);
        
        writeIntensity();
        writeColorCode();

    }

    //intensity method 
    public void getIntensity(BufferedImage image, int height, int width) {

        // save the total number of pixels in the image into the first bin
        intensityBins[0] = height * width;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // retrieve RGB values of current pixel
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                // maybe faster, maybe less safe than the Color method above
                //int red = (pixel & 0x00ff0000) >> 16;
                //int green = (pixel & 0x0000ff00) >> 8;
                //int blue = pixel & 0x000000ff;
                
                // calculate intensity value
                int intensity = (int) (
                        (0.299 * red)
                        + (0.587 * green)
                        + (0.114) * blue
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
        /////////////////////
        ///your code///
        /////////////////
    }

    ///////////////////////////////////////////////
    //add other functions you think are necessary//
    ///////////////////////////////////////////////
    //This method writes the contents of the colorCode matrix to a file named colorCodes.txt.
    public void writeColorCode() {
        /////////////////////
        ///your code///
        /////////////////
    }

    //This method writes the contents of the intensity matrix to a file called intensity.txt
    public void writeIntensity() {
        try {
            ObjectOutputStream outputStream;
            outputStream = new ObjectOutputStream(new FileOutputStream("intensity.txt"));
            outputStream.writeObject(intensityMatrix);
        } catch (Exception e) {
            System.out.println("Error occurred when writing to file");
        }
    }

    public static void main(String[] args) {
        new readImage();
    }

}
