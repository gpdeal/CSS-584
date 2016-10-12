/*
 * Assignment 1
 * 
 * Author: Galen Deal
 * Sample code provided by Dr. Min Chen
 */
package assignment.pkg1;

import java.awt.image.BufferedImage;
import java.lang.Object.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

import java.awt.Color;

// This class was initially provided by Dr. Min Chen, and completed by Galen Deal
public class readImage {

    int imageCount = 1;
    int intensityBins[] = new int[26];
    int intensityMatrix[][] = new int[100][26];
    int colorCodeBins[] = new int[65];
    int colorCodeMatrix[][] = new int[100][65];

    // This method was part of the sample code provided by Dr. Chen and was 
    // completed by Galen Deal
    /* Each image is retrieved from the file.  The height and width are found for the image and the getIntensity and
     * getColorCode methods are called.
     */
    public readImage() {
        while (imageCount < 101) {

            try {

                // Read the next image file
                File file;
                file = new File("images/" + imageCount + ".jpg");
                if (file.exists()) {
                    BufferedImage image = ImageIO.read(file);

                    int height = image.getHeight();
                    int width = image.getWidth();

                    getIntensity(image, height, width);
                    getColorCode(image, height, width);
                } else {
                    System.err.println("File images/" + imageCount + ".jpg does not exist.");
                }

            } catch (Exception e) {
                System.out.println("Error occurred when reading or processing image " + imageCount + ".");
                //failedReads++;
            }
            ++imageCount;
        }

        writeIntensity();
        writeColorCode();

    }

    // This method examines each pixel of the passed image, calculates the 
    // intensity value of that pixel, determines the histogram bin into which
    // that value falls, and increments that bin. Once the entire histogram
    // has been calculated, its values are added to the intensityMatrix.
    public void getIntensity(BufferedImage image, int height, int width) {

        // save the total number of pixels in the image into the first bin
        intensityBins[0] = height * width;

        // For each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // retrieve RGB values of current pixel
                int rgbValues[] = extractRGB(image, x, y);

                // calculate intensity value
                int intensity = (int) ((0.299 * rgbValues[0]) // red
                        + (0.587 * rgbValues[1]) // green
                        + (0.114) * rgbValues[2] // blue
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

    // This method examines each pixel of the passed image, calculates the 
    // color code of that pixel, determines the histogram bin into which
    // that value falls, and increments that bin. Once the entire histogram
    // has been calculated, its values are added to the colorCodeMatrix.
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

    // This method takes as arguments an image and x and y values specifying a
    // single pixel in that image. It then retrieves the RGB values of that
    // pixel and returns them in a 3-cell array with the red value in the first
    // cell, the green value in the second cell, and the blue value in the
    // third cell.
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

    // This method writes the passed int[][] matrix to a file of the passed 
    // filename.
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
