import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png"));
                while (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                    final BufferedImage image;
                    try {
                        File f = fileChooser.getSelectedFile();
                        image = ImageIO.read(f);
                        if (image == null) throw new InvalidFileException(f + " is not a valid image.");
                    } catch (InvalidFileException e) {
                        System.out.println("You must select a file of PNG or JPEG");
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    ImageToAsciiConverter converter = new ImageToAsciiConverter(image);
                    final String ascii = converter.convertImageToAscii(image);

                    try {
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
}
