/*Runhao Huang
October 13th, 2021
This program allows user to choose a raw image each time and process the image in 4 different dithering methods. The methods
include thresholding, random dithering, pattern dithering, and error diffusion. A raw image will be output each time.
And users can decide if they want to continue dithering another image at the end.
*/


import java.io.*;
import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

/*Sample image files provided for this program.
        Image1.raw      a little girl           225 x 180
        Image2.raw      hibiscus flower         300 x 200
        Image3.raw      boys in masks           500 x 500
        Image4.raw      Australian shepherd     500 x 350
        Image 5.raw     trotting horse          500 x 500

 */

public class Main {


    public static void main(String[] args) throws IOException {
        /*Put a loop around all of this so that you can dither a different image file by whatever method
        you choose each time through the loop. This way, you can dither as many images as you
        want by as many methods as you want in one run of the program.*/
        Scanner scnr = new Scanner(System.in);
        int decision = 1;

        //The while loop allows users to dither images continuously in different methods if they want in one run of the program.
        while (decision == 1) {
            System.out.println("What is the input file name?");
            String inputFile = scnr.next();
            System.out.println("What is width of the input?");
            int w = scnr.nextInt();
            System.out.println("What is height of the input?");
            int h = scnr.nextInt();
            System.out.println("What is name of the output file?");
            String outputFile = scnr.next();
            InputStream inputStream = new FileInputStream(inputFile);
            OutputStream outputStream = new FileOutputStream(outputFile);
            System.out.println("What dithering method do you want to use?");
            System.out.print("1 for threshold");
            System.out.print(" 2 for random");
            System.out.print(" 3 for pattern");
            System.out.println(" 4 for error diffusion");
            int ditherMethod = scnr.nextInt();
            switch (ditherMethod) {
                case 1:
                    threshold(inputStream, outputStream, w, h);
                    break;
                case 2:
                    random(inputStream, outputStream, w, h);
                    break;
                case 3:
                    pattern(inputStream, outputStream, w, h);
                    break;
                case 4:
                    errDiff(inputStream, outputStream, w, h);
                    break;
                default:
                    System.out.println("Not a valid choice");
            }

            //This question allows user to choose whether to continue to dither another picture or not.
            System.out.println("Enter 1 to continue dithering another picture. / Enter any other number to quit the program.");
            decision = scnr.nextInt();

        }
    }

    //The threshold dithering method of turning pixel white or black by comparing the original value to 128.
    public static void threshold(InputStream inputStream, OutputStream outputStream, int w, int h) throws IOException {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pixel = inputStream.read();
                if (pixel < 128) {
                    outputStream.write(0);
                } else {
                    outputStream.write(255);
                }
            }
        }
    }

    //The random dithering method of turning pixel white or black by comparing the  original value to a random number each time.
    public static void random(InputStream inputStream, OutputStream outputStream, int w, int h) throws IOException {
        /*In this method, you actually wouldn't have to store the pixel values in a 2d array -- you could just
        store the pixel value in a single int, as you do in the threshold method. But I declare and use a 2D array
        here because you have to do it that way in the pattern and error diffusion methods. This code with help you
        with those methods.*/
        Random rnd = new Random();
        int r;
        int[][] pixels = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixels[i][j] = inputStream.read();
                System.out.print(pixels[i][j]);
                r = rnd.nextInt(256);
                if (pixels[i][j] < r) {
                    outputStream.write(0);
                    System.out.println(" ----- 0");
                } else {
                    outputStream.write(255);
                    System.out.println(" ----- 255");
                }
            }
        }
    }

    //Pattern dithering method turns pixel white or black by comparing the pixel value to the corresponding position in the pattern.
    public static void pattern(InputStream inputStream, OutputStream outputStream, int w, int h) throws IOException {
        /*The pattern to apply is
        8 3 4
        6 1 2
        7 5 9
        Hard-code the pattern into a 2D array */

        //Make the pattern into a 2D array for future sustainability.
        int[][] pattern = {{8, 3, 4}, {6, 1, 2}, {7, 5, 9}};
        int[][] pixels = new int[h][w];

        //The loop outside is to increase the position of target pixel by row number.
        for (int i = 0; i < h; ++i) {
            /*The nested loop is to increase the position of target pixel by column number.
            The target moves from left to right in the first row. Then it gets to the first position in the next row
            and continue the process.
             */
            for (int j = 0; j < w; ++j) {
                pixels[i][j] = inputStream.read();

                //The pixel is scaled to a value between 0 and 9.
                pixels[i][j] = (int) (pixels[i][j] / 25.6);

                //The if statements below can find out the position each pixel corresponds to in the 3*3 pattern.
                //If the pixel value is smaller than the number in the pattern, program outputs 0. Otherwise, program outputs 255.
                if (i % 3 == 0 && j % 3 == 0) {
                    if (pixels[i][j] < pattern[0][0]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                } else if (i % 3 == 0 && j % 3 == 1) {
                    if (pixels[i][j] < pattern[0][1]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                } else if (i % 3 == 0 && j % 3 == 2) {   //j % 3 == 2 is an unnecessary condition but I put it here for clarity.
                    if (pixels[i][j] < pattern[0][2]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                } else if (i % 3 == 1 && j % 3 == 0) {
                    if (pixels[i][j] < pattern[1][0]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                } else if (i % 3 == 1 && j % 3 == 1) {
                    if (pixels[i][j] < pattern[1][1]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                } else if (i % 3 == 1 && j % 3 == 2) {   //j % 3 == 2 is an unnecessary condition but I put it here for clarity.
                    if (pixels[i][j] < pattern[1][2]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                } else if (i % 3 == 2 && j % 3 == 0) {   //i % 3 == 2 is an unnecessary condition but I put it here for clarity.
                    if (pixels[i][j] < pattern[2][0]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                } else if (i % 3 == 2 && j % 3 == 1) {   //i % 3 == 2 is an unnecessary condition but I put it here for clarity.
                    if (pixels[i][j] < pattern[2][1]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                } else {  //The last else statement represent the bottom-right corner of the pattern. This is the last possible position.
                    if (pixels[i][j] < pattern[2][2]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                }
            }
        }
    }


    /*Error diffusion method distributes the error that can be caused by thresholding to pixels around according to a certain pattern.
    And Finally it turns pixel white or black by thresholding.
     */
    public static void errDiff(InputStream inputStream, OutputStream outputStream, int w, int h) throws IOException {
       /* The error diffusion pattern is

                p       7
        3       5       1

        Hard-code this pattern into a 2D array
        }*/

        //The pattern is made into a 2D array for future sustainability.
        int[][] pattern = {{0, 0, 7}, {3, 5, 1}};
        int[][] pixels = new int[h][w];

        //This is the step of reading the values of all pixels of the input image first.
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixels[i][j] = inputStream.read();
            }
        }

        //The nested loops go through every pixel from left to right and then to the next row repeatedly.
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {

                //Calculating the error that can be lead by thresholding.
                int error;
                if (pixels[i][j] < 128) {
                    error = pixels[i][j];
                } else {
                    error = pixels[i][j] - 255;
                }

                //Distributing the portions of the error to pixels around according to the corresponding pattern.
                //Check if the pixel is in the last column.
                if (j + 1 < w) {
                    pixels[i][j + 1] = (int) (pixels[i][j + 1] + (pattern[0][2] / 16.0) * error);
                }

                //Check if the pixel is in the last row and last column.
                if ((i + 1 < h) && (j + 1 < w)) {
                    pixels[i + 1][j + 1] = (int) (pixels[i + 1][j + 1] + (pattern[1][2] / 16.0) * error);
                }

                //Check if the pixel is in the last row.
                if (i + 1 < h) {
                    pixels[i + 1][j] = (int) (pixels[i + 1][j] + (pattern[1][1] / 16.0) * error);
                }

                //Check if the pixel is in the last row and first column.
                if ((j - 1 >= 0) && (i + 1 < h)) {
                    pixels[i + 1][j - 1] = (int) (pixels[i + 1][j - 1] + (pattern[1][0] / 16.0) * error);
                }

            }
        }


        //This is to turn and output the pixel values through thresholding.
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (pixels[i][j] < 128) {
                    outputStream.write(0);
                } else {
                    outputStream.write(255);
                }
            }
        }

    }
}
