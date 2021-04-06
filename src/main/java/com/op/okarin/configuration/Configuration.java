package com.op.okarin.configuration;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Configuration implements Serializable {
    private String wakeWord;
    private List<Command> commands;

    private Configuration() {
    }
}
