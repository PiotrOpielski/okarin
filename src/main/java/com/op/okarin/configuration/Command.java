package com.op.okarin.configuration;

import lombok.Getter;

import java.io.Serializable;
import java.util.function.Predicate;

@Getter
public class Command implements Serializable {
    private String name;
    private CommandPart contains;

    public void processPredicates() {
        contains.processPredicates();
    }

    public Boolean doesComply(String partialResult) {
        return contains.getPredicate().test(partialResult);
    }

    private Command() {
    }
}
