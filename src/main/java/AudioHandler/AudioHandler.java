package AudioHandler;

import Speech.Service.SpeechService;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Properties;

public class AudioHandler implements IAudioHandler{
    private SpeechService service;
    private volatile boolean isListening = false;
    private volatile String bingResult = "";
    private TargetDataLine targetDataLine;
    private long timeOut = 15000;
    private volatile boolean fetchedResult = false;

    public AudioHandler(Properties properties){
        service = new SpeechService(properties);
    }

    @Override
    public String getResult() {
        return this.bingResult;
    }

    @Override
    public void stop() {
        if (this.isListening) {
            this.targetDataLine.stop();
        }
    }

    @Override
    public boolean isFetched() {
        return fetchedResult;
    }

    public void listen(){
        try {
            if (this.isListening) return;
            else {
                this.isListening = true;
                this.fetchedResult = false;
            }
            Mixer.Info[] mixersDescriptors = AudioSystem.getMixerInfo();
            Mixer mixer = null;
            for(Mixer.Info info : mixersDescriptors) {
                Mixer current = AudioSystem.getMixer(info);
                Line.Info[] lines = current.getTargetLineInfo();
                if (info.getDescription().equals(
                        "Direct Audio Device: STM32 AUDIO Streaming in FS Mod, USB Audio, USB Audio"
                )){
                    mixer = current;
                }
            }
            Line.Info[] lines = mixer.getTargetLineInfo();
            if (mixer == null)
                throw new NullPointerException("There is no STM32 in audiocard mode");
            targetDataLine = (TargetDataLine) mixer.getLine(lines[0]);
            if (AudioSystem.isLineSupported(targetDataLine.getLineInfo())){
                //Needs to be logged
                System.out.println("LINE IS SUPPORTED!");
            }else{
                //Needs to be logged
                System.out.println("LINE IS NOT SUPPORTED!");
                throw new LineUnavailableException();
            }
            targetDataLine.open();
            //Needs to be logged
            System.out.println("Starting rec...");
            targetDataLine.start();
            Thread timeOutHadler = new Thread(() -> {
                long startTime = System.currentTimeMillis(),
                        currentTime = System.currentTimeMillis();

                while(currentTime - startTime < timeOut){
                    if (!isListening) break;
                    currentTime = System.currentTimeMillis();
                }
                if (isListening) targetDataLine.stop();
            });
            Thread recordingThread = new Thread(() -> {
                AudioInputStream stream = new AudioInputStream(targetDataLine);
                File recordedFile = new File("Targets/record.wav");
                if (!recordedFile.exists()) {
                    try {
                        if (!recordedFile.getParentFile().exists()) {
                            recordedFile.getParentFile().mkdir();
                        }
                        recordedFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
                try {
                    timeOutHadler.start();
                    AudioSystem.write(stream, AudioFileFormat.Type.WAVE, recordedFile);
                    //Needs to be logged
                    System.out.println("Stopped recording");
                    isListening = false;
                }catch (IOException ioEx){ ioEx.printStackTrace(); }
            });
            //Needs to be logged
            System.out.println("Writing in file!");
            recordingThread.start();
            //Needs to be logged
            System.out.println("Running until manual break(timeout=" + Long.toString(timeOut) + ")!");
            while(this.isListening){}
            if (timeOutHadler.isAlive()) timeOutHadler.interrupt();
            targetDataLine.close();
            //Needs to be logged
            System.out.println("Recorded!");
            Path targetPath = Paths.get("Targets/record.wav");
            byte[] targetBytes = Files.readAllBytes(targetPath);
            //Needs to be logged
            System.out.println("Working...");
            String result = service.recognize(targetBytes, "en-US");
            //Needs to be logged
            System.out.println("Result of recognition: " + result);
            this.bingResult = result;
            this.fetchedResult = true;
            //Needs to be logged
            System.out.println("Done!");
        } catch (LineUnavailableException lUE){
            lUE.printStackTrace();
            System.exit(1);
        } catch (NullPointerException nPE){
            nPE.printStackTrace();
            System.exit(1);
        } catch (IOException iOE){
            iOE.printStackTrace();
            System.exit(1);
        }


    }

}
