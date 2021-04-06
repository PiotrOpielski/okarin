package com.op.okarin.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Register {
    OPEN_BROWSER(() -> System.out.println("Browser opened"));

    private final Runnable action;
}
