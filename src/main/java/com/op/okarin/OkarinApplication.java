package com.op.okarin;


import javax.sound.sampled.LineUnavailableException;

public class OkarinApplication {

    public static void main(String[] args) throws LineUnavailableException {
        VoskRecognizer voskRecognizer = new VoskRecognizer();
        voskRecognizer.setWakeWord("hey now");
        voskRecognizer.setOnWakeWord(voskRecognizer::detectionMode);
        voskRecognizer.prepareData();
        voskRecognizer.detectWakeWord();
    }
}
