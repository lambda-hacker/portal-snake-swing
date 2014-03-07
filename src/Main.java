 import javax.swing.*;

 public class Main {

    public static void main(String[] args) {
       try {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       } catch (Exception e) {
            System.out.println("Native UI failed");
       }
        new GameFrame("Portal Snake 1.0 By Syed M Madani",
                       900, 768);
    }
}
