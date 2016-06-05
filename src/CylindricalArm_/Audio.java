package CylindricalArm_;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.Player;


public class Audio {
    private String filename;
    private Player player; 


    public Audio(String filename) {
        this.filename = filename;
    }
    public void close() { if (player != null) player.close(); }
    public void play() {
        try {

            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);

                player = new Player(bis);
            
            
        }
        catch (Exception e) {
            System.out.println("ERROR404: FILE NOT FOUND" + filename);
            System.out.println(e);
        }


        new Thread() {
            public void run() {
                try { player.play(); }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();

    }

}