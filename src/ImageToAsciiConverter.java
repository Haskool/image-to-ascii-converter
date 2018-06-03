import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

class ImageToAsciiConverter {

    private static final char[] greyToChar;
    /*
    * initialises greyToChar
    * the greyVal is the index and that maps to the corresponding char
    * I would add a map from index of ranges to index of chars but I feel this is overkill
    * for this simple programme
    * */
    static {
        greyToChar = new char[256];
        int[][] ranges = new int[][]{
                {0, 50}, {50, 70}, {70, 100}, {100, 130},
                {130, 160}, {160, 180}, {180, 200},
                {200, 230}, {230, 256}
        };
        char[] chars = {' ', '"', '!', ':', 'I', ':', '*', '#', ' '};

        for(int rangeIndex = 0; rangeIndex < chars.length; rangeIndex++){
            for (int j = ranges[rangeIndex][0]; j < ranges[rangeIndex][1]; j++) {
                greyToChar[j] = chars[rangeIndex];
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "gif", "png"));
                while (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File f = fileChooser.getSelectedFile();
                        final BufferedImage image = ImageIO.read(f);
                        if (image == null) throw new IllegalArgumentException(f + " is not a valid image.");
                        final String ascii = convertImageToAscii(image);
                        final JTextArea textArea = new JTextArea(ascii, 800, 800);
                        textArea.setBackground(Color.GRAY);
                        textArea.setForeground(Color.WHITE);
                        textArea.setFont(new Font("Monospaced", Font.BOLD, 5));
                        textArea.setEditable(false);
                        final JDialog dialog = new JOptionPane(new JScrollPane(textArea), JOptionPane.PLAIN_MESSAGE).createDialog("ImageToAscii");
                        dialog.setResizable(true);
                        dialog.setVisible(true);
                        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
                        out.print(ascii);
                        System.setOut(out);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
                System.exit(0);
            }
        });
    }

    /*
    * This iterates through the image brightness
    * It converts the brightness of each pixel to a char
    * Returns string to be outputted
    * */
    public static String convertImageToAscii(BufferedImage image){
        int h = image.getHeight();
        int w = image.getWidth();
        char[][] result = new char[h][w];
        for(int y = 0; y < h; y++){
            for(int x = 1; x < w; x++){
                if(w % 10 == 0){
                    double leftBrightness = getGreyValOfRgb(image.getRGB(x, y));
                    int averageGreyness = (int) leftBrightness;
                    result[y][x] = greyToChar[averageGreyness];

                }
                else {
                    double leftBrightness = getGreyValOfRgb(image.getRGB(x, y));
                    int averageGreyness = (int) leftBrightness;
                    result[y][x] = greyToChar[averageGreyness];
                }
            }
        }
        StringBuilder s = new StringBuilder();
        for (char[] line: result
             ) {
            s.append(java.util.Arrays.toString(line) + "\n");
        }
        return s.toString();
    }

    public static double getGreyValOfRgb(int rgb){
        Color c = new Color(rgb);
        int R = c.getRed();
        int G = c.getGreen();
        int B = c.getBlue();
        return ((0.299 * R) + (0.587 * G) + (0.114 * B));
    }

}
