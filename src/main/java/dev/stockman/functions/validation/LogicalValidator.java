package dev.stockman.functions.validation;

import java.util.Set;
import java.util.function.Function;

public interface LogicalValidator<I> extends Function<I, Set<Violation>> { }
