package Init;

import AudioHandler.AudioHandler;
import GUI.GUI;
import Speech.Service.SpeechService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Init {
    public static void main(String[] args){
        Properties bingProps= new Properties();
        //Needs to be logged
        System.out.println("#Configuration reading...");
        try{
            bingProps.load(ClassLoader.getSystemResourceAsStream("Configuration/bing.properties"));
        }catch (IOException e){
            //Needs to be logged
            System.out.println("[!]Configuration load error!");
            System.exit(1);
        }catch (NullPointerException e){
            //Needs to be logged
            System.out.println("[!]Configuration is not found!");
            System.exit(1);
        }
        //Needs to be logged
        System.out.println("[+]Configuration is loaded!");

        AudioHandler audioHandler = new AudioHandler(bingProps);
        GUI gui = new GUI(audioHandler);
        gui.setVisible(true);
    }
}
