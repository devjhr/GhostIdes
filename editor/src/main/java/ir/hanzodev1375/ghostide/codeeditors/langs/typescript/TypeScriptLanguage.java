/**
 * Comment by ghost ide
 *
 * @author: Ninjacoder
 */
package ir.hanzodev1375.ghostide.codeeditors.langs.typescript;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.github.rosemoe.sora.lang.EmptyLanguage;
import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.lang.QuickQuoteHandler;
import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.completion.CompletionHelper;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.completion.IdentifierAutoComplete;
import io.github.rosemoe.sora.lang.completion.SimpleSnippetCompletionItem;
import io.github.rosemoe.sora.lang.completion.SnippetDescription;
import io.github.rosemoe.sora.lang.completion.snippet.CodeSnippet;
import io.github.rosemoe.sora.lang.completion.snippet.parser.CodeSnippetParser;
import io.github.rosemoe.sora.lang.format.Formatter;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandleResult;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.lang.styling.Styles;
import io.github.rosemoe.sora.lang.styling.StylesUtils;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.text.TextUtils;
import io.github.rosemoe.sora.util.MyCharacter;
import io.github.rosemoe.sora.widget.SymbolPairMatch;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Character.isWhitespace;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class TypeScriptLanguage implements Language {

  // Snippets
  private static final CodeSnippet FOR_SNIPPET =
      CodeSnippetParser.parse("for (${1:i} = ${2:0}; ${1:i} < ${3:10}; ${1:i}++) {\n    $0\n}");

  private static final CodeSnippet FUN_SNIPPET =
      CodeSnippetParser.parse("function ${1:name}(${2:params}) {\n    $0\n}");

  private static final CodeSnippet IF_SNIPPET =
      CodeSnippetParser.parse("if (${1:condition}) {\n    $0\n}");

  private static final CodeSnippet ARROW_FN_SNIPPET =
      CodeSnippetParser.parse("(${1:params}) => {\n    $0\n}");

  private static final CodeSnippet CLASS_SNIPPET =
      CodeSnippetParser.parse("class ${1:Name} {\n    ${0}\n}");

  private static final CodeSnippet INTERFACE_SNIPPET =
      CodeSnippetParser.parse("interface ${1:Name} {\n    $0\n}");

  private IdentifierAutoComplete autoComplete;

  private final TsAnalyzer manager;

  private final TypeScriptQuoteHandler quoteHandler = new TypeScriptQuoteHandler();

  // کلمات کلیدی تایپ‌اسکریپت (برای تکمیل)
  private static final String[] KEYWORDS = {
    "as",
    "break",
    "case",
    "catch",
    "class",
    "const",
    "continue",
    "debugger",
    "default",
    "delete",
    "do",
    "else",
    "enum",
    "export",
    "extends",
    "false",
    "finally",
    "for",
    "from",
    "function",
    "get",
    "if",
    "implements",
    "import",
    "in",
    "instanceof",
    "interface",
    "let",
    "new",
    "null",
    "of",
    "package",
    "private",
    "protected",
    "public",
    "require",
    "return",
    "set",
    "static",
    "super",
    "switch",
    "this",
    "throw",
    "true",
    "try",
    "typeof",
    "var",
    "void",
    "while",
    "with",
    "yield",
    "any",
    "boolean",
    "never",
    "number",
    "string",
    "symbol",
    "unknown",
    "void",
    "module",
    "namespace",
    "declare",
    "type",
    "keyof",
    "infer",
    "readonly",
    "unique",
    "assert",
    "await",
    "async"
  };

  public TypeScriptLanguage() {
    autoComplete = new IdentifierAutoComplete(KEYWORDS);
    manager = new TsAnalyzer();
  }

  @NonNull
  @Override
  public AnalyzeManager getAnalyzeManager() {
    return manager;
  }

  @Nullable
  @Override
  public QuickQuoteHandler getQuickQuoteHandler() {
    return quoteHandler;
  }

  @Override
  public void destroy() {
    autoComplete = null;
  }

  @Override
  public int getInterruptionLevel() {
    return INTERRUPTION_LEVEL_STRONG;
  }

  @Override
  public void requireAutoComplete(
      @NonNull ContentReference content,
      @NonNull CharPosition position,
      @NonNull CompletionPublisher publisher,
      @NonNull Bundle extraArguments) {
    var prefix =
        CompletionHelper.computePrefix(content, position, MyCharacter::isJavaIdentifierPart);
    final var idt = manager.getSyncIdentifiers();
    if (idt != null && autoComplete != null) {
      autoComplete.requireAutoComplete(content, position, prefix, publisher, idt);
    }
    if ("for".startsWith(prefix) && !prefix.isEmpty()) {
      publisher.addItem(
          new SimpleSnippetCompletionItem(
              "for",
              "Snippet - For loop",
              new SnippetDescription(prefix.length(), FOR_SNIPPET, true)));
    }
    if ("fun".startsWith(prefix) && !prefix.isEmpty()) {
      publisher.addItem(
          new SimpleSnippetCompletionItem(
              "fun",
              "Snippet - Function",
              new SnippetDescription(prefix.length(), FUN_SNIPPET, true)));
    }
    if ("if".startsWith(prefix) && !prefix.isEmpty()) {
      publisher.addItem(
          new SimpleSnippetCompletionItem(
              "if",
              "Snippet - If statement",
              new SnippetDescription(prefix.length(), IF_SNIPPET, true)));
    }
    if ("arrow".startsWith(prefix) && !prefix.isEmpty()) {
      publisher.addItem(
          new SimpleSnippetCompletionItem(
              "arrow",
              "Snippet - Arrow function",
              new SnippetDescription(prefix.length(), ARROW_FN_SNIPPET, true)));
    }
    if ("class".startsWith(prefix) && !prefix.isEmpty()) {
      publisher.addItem(
          new SimpleSnippetCompletionItem(
              "class",
              "Snippet - Class",
              new SnippetDescription(prefix.length(), CLASS_SNIPPET, true)));
    }
    if ("interface".startsWith(prefix) && !prefix.isEmpty()) {
      publisher.addItem(
          new SimpleSnippetCompletionItem(
              "interface",
              "Snippet - Interface",
              new SnippetDescription(prefix.length(), INTERFACE_SNIPPET, true)));
    }
  }

  @Override
  public int getIndentAdvance(@NonNull ContentReference text, int line, int column) {
    var content = text.getLine(line).substring(0, column);
    return getIndentAdvance(content);
  }

  int getIndentAdvance(String content) {
    try {
      TypeScriptLexer lexer = new TypeScriptLexer(CharStreams.fromString(content));
      Token token;
      int to = 0;
      while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
        switch (token.getType()) {
          case TypeScriptLexer.OpenBrace:
            to++;
            break;
          case TypeScriptLexer.CloseBrace:
            to--;
            break;
        }
      }
      to = Math.max(0, to);
      return to * 2;
    } catch (Exception err) {
      err.printStackTrace();
    }
    return 0;
  }

  private final NewlineHandler[] newlineHandlers = new NewlineHandler[] {new BraceHandler()};

  @Override
  public boolean useTab() {
    // spaces
    return false;
  }

  @NonNull
  @Override
  public Formatter getFormatter() {
    return EmptyLanguage.EmptyFormatter.INSTANCE;
  }

  @Override
  public SymbolPairMatch getSymbolPairs() {
    return new SymbolPairMatch.DefaultSymbolPairs();
  }

  @Override
  public NewlineHandler[] getNewlineHandlers() {
    return newlineHandlers;
  }

  private static String getNonEmptyTextBefore(CharSequence text, int index, int length) {
    while (index > 0 && isWhitespace(text.charAt(index - 1))) index--;
    return text.subSequence(Math.max(0, index - length), index).toString();
  }

  private static String getNonEmptyTextAfter(CharSequence text, int index, int length) {
    while (index < text.length() && isWhitespace(text.charAt(index))) index++;
    return text.subSequence(index, Math.min(index + length, text.length())).toString();
  }

  class BraceHandler implements NewlineHandler {

    @Override
    public boolean matchesRequirement(
        @NonNull Content text, @NonNull CharPosition position, @Nullable Styles style) {
      var line = text.getLine(position.line);
      return !StylesUtils.checkNoCompletion(style, position)
          && getNonEmptyTextBefore(line, position.column, 1).equals("{")
          && getNonEmptyTextAfter(line, position.column, 1).equals("}");
    }

    @NonNull
    @Override
    public NewlineHandleResult handleNewline(
        @NonNull Content text,
        @NonNull CharPosition position,
        @Nullable Styles style,
        int tabSize) {
      var line = text.getLine(position.line);
      int index = position.column;
      var beforeText = line.subSequence(0, index).toString();
      var afterText = line.subSequence(index, line.length()).toString();
      return handleNewline(beforeText, afterText, tabSize);
    }

    @NonNull
    public NewlineHandleResult handleNewline(String beforeText, String afterText, int tabSize) {
      int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
      int advanceBefore = getIndentAdvance(beforeText);
      int advanceAfter = getIndentAdvance(afterText);
      String text;
      StringBuilder sb =
          new StringBuilder("\n")
              .append(TextUtils.createIndent(count + advanceBefore, tabSize, useTab()))
              .append('\n')
              .append(text = TextUtils.createIndent(count + advanceAfter, tabSize, useTab()));
      int shiftLeft = text.length() + 1;
      return new NewlineHandleResult(sb, shiftLeft);
    }
  }
}
