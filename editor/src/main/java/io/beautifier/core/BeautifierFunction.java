package io.beautifier.core;

@FunctionalInterface
public interface BeautifierFunction {

  String beautify(String sourceText, Options<?> options);
}
