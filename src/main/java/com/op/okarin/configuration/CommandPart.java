package com.op.okarin.configuration;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

@Getter
public class CommandPart implements Serializable {
    private String value;
    private List<CommandPart> or;
    private List<CommandPart> and;
    private Predicate<String> predicate;

    public void processPredicates() {
        predicate = (s) -> s.contains(value);
        if (or != null) {
            or.forEach(CommandPart::processPredicates);
            or.forEach(a -> predicate = predicate.or(a.getPredicate()));
        }
        if (and != null) {
            and.forEach(CommandPart::processPredicates);
            and.forEach(a -> predicate = predicate.and(a.getPredicate()));
        }
    }

    private CommandPart() {
    }
}
