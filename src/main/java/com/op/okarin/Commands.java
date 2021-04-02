package com.op.okarin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Commands {
    OPEN_BROWSER("open browser", () -> System.out.println("Browser opened"));
    private String command;
    private Runnable action;
}
