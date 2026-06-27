package io.beautifier.core;

import io.beautifier.css.CSSBeautifier;
import io.beautifier.css.CSSOptions;
import io.beautifier.html.HTMLBeautifier;
import io.beautifier.html.HTMLOptions;
import io.beautifier.javascript.JavaScriptBeautifier;
import io.beautifier.javascript.JavaScriptOptions;

/**
 * Recommended Android entry point for this library.
 *
 * <p>Functionally identical to calling {@code JavaScriptBeautifier.beautify(...)}, {@code
 * CSSBeautifier.beautify(...)}, or {@code HTMLBeautifier.beautify(...)} directly, except the actual
 * beautify call runs on a dedicated thread with a large stack (see {@link AndroidSafeExecutor}) so
 * large files don't risk a {@link StackOverflowError} on Android's smaller default thread stack.
 * Safe to call from the UI thread (it blocks until done) or from a background thread/executor of
 * your own.
 */
public final class AndroidBeautify {

  private AndroidBeautify() {}

  public static String js(String sourceText, JavaScriptOptions options) {
    return AndroidSafeExecutor.runUnchecked(
        () -> JavaScriptBeautifier.beautify(sourceText, options));
  }

  public static String js(String sourceText, JavaScriptOptions options, long stackSizeBytes) {
    return AndroidSafeExecutor.runUnchecked(
        () -> JavaScriptBeautifier.beautify(sourceText, options), stackSizeBytes);
  }

  public static String css(String sourceText, CSSOptions options) {
    return AndroidSafeExecutor.runUnchecked(() -> CSSBeautifier.beautify(sourceText, options));
  }

  public static String css(String sourceText, CSSOptions options, long stackSizeBytes) {
    return AndroidSafeExecutor.runUnchecked(
        () -> CSSBeautifier.beautify(sourceText, options), stackSizeBytes);
  }

  public static String html(String sourceText, HTMLOptions options) {
    return AndroidSafeExecutor.runUnchecked(() -> HTMLBeautifier.beautify(sourceText, options));
  }

  public static String html(String sourceText, HTMLOptions options, long stackSizeBytes) {
    return AndroidSafeExecutor.runUnchecked(
        () -> HTMLBeautifier.beautify(sourceText, options), stackSizeBytes);
  }
}
