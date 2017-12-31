import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.random.org/integers/?rgbArray=384&min=0&max=255&col=3&base=10&format=plain&rnd=new")
                .get()
                .build();

        Response response;
        String responseString = null;

        try {
            response = client.newCall(request).execute();
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String[] lines = responseString.split("\n");
        int[][] rgbArray = new int[128][3];

        try{
            for (int i = 0; i < rgbArray.length; i++) {
                String[] spl = lines[i].split("\\s");
                rgbArray[i][0] = Integer.parseInt(spl[0]);
                rgbArray[i][1] = Integer.parseInt(spl[1]);
                rgbArray[i][2] = Integer.parseInt(spl[2]);
            }
        }catch(NumberFormatException e){
            e.printStackTrace();
        }

        BufferedImage img = mapPixels(128, 128, rgbArray);
        saveImage(img, "./test.bmp");
    }

    private static BufferedImage mapPixels(int sizeX, int sizeY, int[][] rgbArray) {
        final BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                image.setRGB(x, y, new Color(rgbArray[x][0], rgbArray[x][1], rgbArray[x][2]).getRGB());
            }
        }
        return image;
    }

    private static void saveImage(final BufferedImage bufferedImage, final String path) {
        try {
            RenderedImage renderedImage = bufferedImage;
            ImageIO.write(renderedImage, "bmp", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
