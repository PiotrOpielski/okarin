package com.op.okarin;

import lombok.Setter;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class VoskRecognizer {

    @Setter
    private Runnable onWakeWord;
    @Setter
    private String wakeWord;
    private TargetDataLine line;
    private Recognizer recognizer;

    public void prepareData() {
        try {
            AudioFormat format = new AudioFormat(16000, 16,
                    1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);

            LibVosk.setLogLevel(LogLevel.DEBUG);

            Model model = new Model("model");
            recognizer = new Recognizer(model, 16000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void detectWakeWord() {
        line.start();
        byte[] targetData = new byte[4096];
        while (true) {
            line.read(targetData, 0, targetData.length);
            if (recognizer.acceptWaveForm(targetData, targetData.length)) {
                if (recognizer.getPartialResult().toLowerCase().contains(wakeWord)) {
                    onWakeWord.run();
                    break;
                }
            }
        }
        line.close();
    }
}
