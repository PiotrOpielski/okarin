package com.op.okarin;

import com.op.okarin.configuration.Command;
import com.op.okarin.configuration.Register;
import lombok.Setter;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.Optional;

public class VoskRecognizer {

    @Setter
    private String wakeWord;
    private TargetDataLine line;
    private Model model;

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

            model = new Model("model");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void detectWakeWord() {
        line.start();
        byte[] targetData = new byte[4096];
        try (Recognizer recognizer = new Recognizer(model, 16000)) {

            while (true) {
                line.read(targetData, 0, targetData.length);
                if (recognizer.acceptWaveForm(targetData, targetData.length)) {
                    if (recognizer.getPartialResult().toLowerCase().contains(wakeWord)) {
                        detectionMode();
                        break;
                    }
                }
            }
        }
        line.close();
    }

    public void detectionMode() {
        System.out.println("Listening to the command");

        line.start();
        try (Recognizer recognizer = new Recognizer(model, 16000)) {
            byte[] targetData = new byte[4096];
            Optional<Command> detected;
            while (true) {
                line.read(targetData, 0, targetData.length);
                if (recognizer.acceptWaveForm(targetData, targetData.length)) {
                    System.out.println(recognizer.getPartialResult());

                    detected = OkarinApplication.configuration.getCommands().stream()
                            .filter(s -> s.doesComply(recognizer.getPartialResult()))
                            .findAny();

                    if (detected.isPresent()) {
                        Register.valueOf(detected.get().getName()).getAction().run();
                        break;
                    }
                }
            }
        }
        line.close();
        detectWakeWord();
    }
}
