import java.awt.*;
import java.awt.image.BufferedImage;

class ImageToAsciiConverter {

    private final char[] greyValToChar;

    public ImageToAsciiConverter(){
        greyValToChar = new char[256];
        int[][] ranges = new int[][]{
                {0, 50}, {50, 70}, {70, 100}, {100, 130},
                {130, 160}, {160, 180}, {180, 200},
                {200, 230}, {230, 256}
        };
        char[] chars = {' ', '"', '!', ':', 'I', ':', '*', '#', ' '};

        for(int rangeIndex = 0; rangeIndex < chars.length; rangeIndex++){
            for (int j = ranges[rangeIndex][0]; j < ranges[rangeIndex][1]; j++) {
                greyValToChar[j] = chars[rangeIndex];
            }
        }
    }

    public String convertImageToAscii(BufferedImage image){
        int h = image.getHeight();
        int w = image.getWidth();
        if(h > w){
            double k = h/w;
            w = 200;
            h = (int) (w*k);
            image = resize(image, w, h);
        } else {
            double k = w/h;
            h = 200;
            w = (int) (h*k);
            image = resize(image, w, h);
        }
        char[][] result = new char[h][w];
        for(int y = 0; y < h; y++){
            for(int x = 1; x < w; x++){
                    double greyVal = getGreyValOfRgb(image.getRGB(x, y));
                    int roundedGreyVal = (int) Math.round(greyVal);
                    result[y][x] = greyValToChar[roundedGreyVal];
            }
        }
        StringBuilder s = new StringBuilder();
        for (char[] row: result) {
            s.append(new String(row)).append("\n");
        }
        return s.toString();
    }

    public double getGreyValOfRgb(int rgb){
        Color c = new Color(rgb);
        int R = c.getRed();
        int G = c.getGreen();
        int B = c.getBlue();
        return ((0.299 * R) + (0.587 * G) + (0.114 * B));
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

}
