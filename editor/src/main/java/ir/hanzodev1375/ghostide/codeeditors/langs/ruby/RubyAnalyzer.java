/**
 * Comment by ghost ide
 *
 * @author: Ninjacoder
 */
package ir.hanzodev1375.ghostide.codeeditors.langs.ruby;

import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult;
import io.github.rosemoe.sora.lang.completion.IdentifierAutoComplete;
import io.github.rosemoe.sora.lang.styling.Span;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import ir.hanzodev1375.ghostide.codeeditors.colorscheme.GhostColorScheme;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.CodeAnalyzer;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.IncrementalToken;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.LineState;
import java.util.ArrayList;
import java.util.List;

public class RubyAnalyzer extends CodeAnalyzer {

  public RubyAnalyzer() {
    super(Ruby.class);
  }

  @Override
  protected int[][] getMultilineTokenStartEndTypes() {
    return new int[][] {new int[] {Ruby.ML_COMMENT}, new int[] {Ruby.ML_COMMENT}};
  }

  @Override
  protected int[] getCodeBlockTokens() {
    return new int[] {
      Ruby.LEFT_RBRACKET, Ruby.RIGHT_RBRACKET, Ruby.LEFT_SBRACKET, Ruby.RIGHT_SBRACKET, Ruby.END
    };
  }

  @Override
  protected boolean isIdentifierToken(int tokenType) {
    return tokenType == Ruby.ID;
  }

  @Override
  protected void handleIncompleteToken(IncrementalToken token) {

    token.type = Ruby.ID;
  }

  @Override
  protected List<Span> generateSpans(LineTokenizeResult<LineState, IncrementalToken> tokens) {
    List<Span> spans = new ArrayList<>();
    int prevToken = -1;
    for (int i = 0; i < tokens.tokens.size(); i++) {
      final var token = tokens.tokens.get(i);
      int type = token.getType();
      int offset = token.getStartIndex();
      int length = 1;
      if (i + 1 < tokens.tokens.size()) {
        length = tokens.tokens.get(i + 1).getStartIndex() - offset;
      }
      switch (type) {
        case Ruby.SL_COMMENT:
        case Ruby.ML_COMMENT:
          spans.add(Span.obtain(offset, EditorColorScheme.COMMENT));
          break;
        case Ruby.REQUIRE:
        case Ruby.END:
        case Ruby.DEF:
        case Ruby.RETURN:
        case Ruby.PIR:
        case Ruby.IF:
        case Ruby.ELSE:
        case Ruby.ELSIF:
        case Ruby.UNLESS:
        case Ruby.WHILE:
        case Ruby.RETRY:
        case Ruby.BREAK:
        case Ruby.FOR:
        case Ruby.AND:
        case Ruby.OR:
        case Ruby.NOT:
        case Ruby.NIL:
        case Ruby.TRUE:
        case Ruby.FALSE:
        case Ruby.LEFT_RBRACKET:
        case Ruby.RIGHT_RBRACKET:
          spans.add(Span.obtain(offset, EditorColorScheme.KEYWORD));
          break;
        case Ruby.LITERAL:
        case Ruby.INT:
        case Ruby.FLOAT:
          spans.add(Span.obtain(offset, EditorColorScheme.LITERAL));
          break;
        case Ruby.PLUS:
        case Ruby.MINUS:
        case Ruby.MUL:
        case Ruby.DIV:
        case Ruby.MOD:
        case Ruby.EXP:
        case Ruby.EQUAL:
        case Ruby.NOT_EQUAL:
        case Ruby.GREATER:
        case Ruby.LESS:
        case Ruby.LESS_EQUAL:
        case Ruby.GREATER_EQUAL:
        case Ruby.ASSIGN:
        case Ruby.PLUS_ASSIGN:
        case Ruby.MINUS_ASSIGN:
        case Ruby.MUL_ASSIGN:
        case Ruby.DIV_ASSIGN:
        case Ruby.MOD_ASSIGN:
        case Ruby.EXP_ASSIGN:
        case Ruby.BIT_AND:
        case Ruby.BIT_OR:
        case Ruby.BIT_XOR:
        case Ruby.BIT_NOT:
        case Ruby.BIT_SHL:
        case Ruby.BIT_SHR:
          spans.add(Span.obtain(offset, EditorColorScheme.OPERATOR));
          break;
        case Ruby.ID:
          {
            int color = GhostColorScheme.TEXT_NORMAL;
            if (prevToken == Ruby.DEF) {
              color = GhostColorScheme.IDENTIFIER_VAR;
            } else if (prevToken == Ruby.UNLESS) {
              color = GhostColorScheme.ATTRIBUTE_NAME;
            } else if (prevToken == Ruby.IF || prevToken == Ruby.ELSIF) {
              color = GhostColorScheme.LITERAL;
            }
            spans.add(Span.obtain(offset, color));
            break;
          }

        case Ruby.WS:
          break;
        default:
          spans.add(Span.obtain(offset, EditorColorScheme.TEXT_NORMAL));
          break;
      }
      if (type != Ruby.WS) {
        prevToken = type;
      }
    }
    return spans;
  }

  @Override
  protected boolean isCodeBlockStart(IncrementalToken token) {
    int type = token.getType();
    return type == Ruby.IF
        || type == Ruby.ELSIF
        || type == Ruby.ELSE
        || type == Ruby.UNLESS
        || type == Ruby.FOR
        || type == Ruby.DEF;
  }

  @Override
  protected boolean isCodeBlockEnd(IncrementalToken token) {

    return token.getType() == Ruby.END;
  }

  public IdentifierAutoComplete.SyncIdentifiers getSyncIdentifiers() {
    return syncIdentifiers;
  }
}
