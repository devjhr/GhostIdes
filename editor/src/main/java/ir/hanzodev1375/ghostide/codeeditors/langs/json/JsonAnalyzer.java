package ir.hanzodev1375.ghostide.codeeditors.langs.json;

import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult;
import io.github.rosemoe.sora.lang.styling.Spans;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.IncrementalToken;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.LineState;
import io.github.rosemoe.sora.lang.styling.Span;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.CodeAnalyzer;
import java.util.ArrayList;
import java.util.List;

public class JsonAnalyzer extends CodeAnalyzer {
  public JsonAnalyzer() {
    super(JSONLexer.class);
  }

  @Override
  protected int[][] getMultilineTokenStartEndTypes() {
    return new int[][] {new int[] {-1}, new int[] {-1}};
  }

  @Override
  protected int[] getCodeBlockTokens() {
    return new int[] {JSONLexer.LBRACE, JSONLexer.RBRACE};
  }

  @Override
  protected boolean isIdentifierToken(int tokenType) {
    return false;
  }

  @Override
  protected List<Span> generateSpans(LineTokenizeResult<LineState, IncrementalToken> tokens) {
    List<Span> spans = new ArrayList<>();
    int pretoken = -1;

    for (int i = 0; i < tokens.tokens.size(); ++i) {
      final var token = tokens.tokens.get(i);
      final int type = token.getType();
      final int offset = token.getStartIndex();
      final String text = token.getText();
      switch (type) {
        case JSONLexer.COLON:
        case JSONLexer.COMMA:
          spans.add(Span.obtain(offset, EditorColorScheme.OPERATOR));
          break;
        case JSONLexer.LBRACE:
        case JSONLexer.LBRACKET:
        case JSONLexer.RBRACE:
        case JSONLexer.RBRACKET:
          spans.add(Span.obtain(offset, EditorColorScheme.ATTRIBUTE_NAME));
          break;

        case JSONLexer.STRING:
          spans.add(Span.obtain(offset, EditorColorScheme.LITERAL));
          break;
        case JSONLexer.TRUE:
        case JSONLexer.FALSE:
        case JSONLexer.NULL:
          spans.add(Span.obtain(offset, EditorColorScheme.KEYWORD));
          break;
      }
    }

    return spans;
  }

  @Override
  protected void handleIncompleteToken(IncrementalToken token) {}
}
