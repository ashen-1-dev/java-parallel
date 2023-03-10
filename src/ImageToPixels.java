import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class ImageToPixels {

    public static void main(String[] args) {
        try {
            // Upload the image
            BufferedImage image = ImageIO.read(new File("src/1024.jpg"));

            int threshold = 135;
            long start = System.nanoTime();
            //Modify pixel here
            int[][] monochromeArray = convertToMonochrome(image, threshold);
            int[][] erodedArray = erode(monochromeArray, 3);
            BufferedImage modifiedImage = convert(image, erodedArray);
            long end = System.nanoTime();


            File file = new File("G:\\parallel\\1\\pr_1\\src\\test.jpg");
            ImageIO.write(modifiedImage, "jpg", file);

            System.out.println("Затраченное время: " + (end - start)/Math.pow(10, 9) + " секунды");

        } catch (Exception exc) {
            System.out.println("Interrupted: " + exc.getMessage());
        }
    }

    public static int[][] convertToMonochrome(BufferedImage image, int threshold) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //Retrieving contents of a pixel
                int pixel = image.getRGB(x,y);
                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                //Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int intensity = (red + green + blue)/3;

                if(intensity > threshold) {
                    result[y][x] = 1;
                } else {
                    result[y][x] = 0;
                }
            }
        }
        return result;
    }
    public static BufferedImage convert(BufferedImage image, int[][] eroded) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //Retrieving contents of a pixel
                int pixel = image.getRGB(x,y);
                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                //Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                if (eroded[y][x] == 1) {
                    red = 255;
                    green = 255;
                    blue = 255;
                } else  {
                    red = 0;
                    green = 0;
                    blue = 0;
                }

                color = new Color(red, green, blue);

                image.setRGB(x, y, color.getRGB());

            }
        }
        return image;
    }

    public static int[][] erode(int[][] array, int step)
    {
        int height = array.length;
        int width = array[0].length;
        int[][] result = new int[height][width];
        for (int y = 0; y < height; y += step) {
            for (int x = 0; x < width; x += step) {
                int current = array[y][x];

                if (x - 1 < 0 || x + 1 > width - 1 || y - 1 < 0 || y + 1 > height - 1) {
                    result[y][x] = 0;
                    continue;
                }


                if(
                        current == 1 &&
                                array[y - 1][x - 1] == current &&
                                array[y][x - 1] == current &&
                                array[y + 1][x - 1] == current &&
                                array[y - 1][x] == current &&
                                array[y + 1][x] == current &&
                                array[y - 1][x + 1] == current &&
                                array[y][x + 1] == current &&
                                array[y + 1][x + 1] == current
                ) {
                    result[y][x] = 1;
                } else  {
                    result[y][x] = 0;
                }
            }
        }
        return result;
    }

}
