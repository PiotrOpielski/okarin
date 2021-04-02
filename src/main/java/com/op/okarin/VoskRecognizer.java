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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VoskRecognizer {

    @Setter
    private Runnable onWakeWord;
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
                        onWakeWord.run();
                        break;
                    }
                }
            }
        }
        line.close();
    }

    public void detectionMode() {
        System.out.println("Listening to the command");

        List<String> commands = Arrays.stream(Commands.values())
                .map(Commands::getCommand)
                .collect(Collectors.toList());


        line.start();
        try (Recognizer recognizer = new Recognizer(model, 16000)) {
            byte[] targetData = new byte[4096];
            Optional<String> detected;
            while (true) {
                line.read(targetData, 0, targetData.length);
                if (recognizer.acceptWaveForm(targetData, targetData.length)) {
                    System.out.println(recognizer.getPartialResult());
                    detected = commands.stream()
                            .filter(s -> recognizer.getPartialResult().toLowerCase().contains(s))
                            .findAny();
                    if (detected.isPresent()) {
                        Commands.OPEN_BROWSER.getAction().run();
                        break;
                    }
                }
            }
        }
        line.close();
        detectWakeWord();
    }
}
