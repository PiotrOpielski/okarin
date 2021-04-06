package com.op.okarin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.op.okarin.configuration.Command;
import com.op.okarin.configuration.Configuration;

import java.io.File;
import java.io.IOException;

public class OkarinApplication {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static Configuration configuration;

    public static void main(String[] args) throws IOException {
        File configurationFile = new File("data/configuration.json");
        configuration = objectMapper.readValue(configurationFile, Configuration.class);
        configuration.getCommands().forEach(Command::processPredicates);

        VoskRecognizer voskRecognizer = new VoskRecognizer();
        voskRecognizer.setWakeWord(configuration.getWakeWord());
        voskRecognizer.prepareData();
        voskRecognizer.detectWakeWord();
    }
}
