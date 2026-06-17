package ir.hanzodev1375.ghostide.codeeditors.langs.xml;

import io.github.rosemoe.sora.lang.styling.Span;
import static ir.hanzodev1375.ghostide.codeeditors.colorscheme.GhostColorScheme.*;
import ir.hanzodev1375.ghostide.codeeditors.colorscheme.GhostColorScheme;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.CodeAnalyzer;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.IncrementalToken;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.LineState;
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult;

import ir.hanzodev1375.ghostide.codeeditors.langs.html.HTMLLexer;
import java.util.ArrayList;
import java.util.List;

public class XmlAnalyzer extends CodeAnalyzer {

  public XmlAnalyzer() {
    super(HTMLLexer.class);
  }

  @Override
  protected int[][] getMultilineTokenStartEndTypes() {

    return new int[][] {{-1}, {-1}};
  }

  @Override
  protected int[] getCodeBlockTokens() {

    return new int[0];
  }

  @Override
  protected boolean isIdentifierToken(int tokenType) {
    return false;
  }

  @Override
  protected void handleIncompleteToken(IncrementalToken token) {}

  @Override
  protected List<Span> generateSpans(final LineTokenizeResult<LineState, IncrementalToken> tokens) {
    final List<Span> spans = new ArrayList<>();
    int pretoken = -1;
    //    getManagedStyles().addLineStyle(new LineSideIcon(2, new ColorDrawable(Color.CYAN)));
    //    getManagedStyles().addLineStyle(new LineSideIcon(32, new ColorDrawable(Color.BLUE)));
    //    getManagedStyles().addLineStyle(new LineBackground(2, new ConstColor(Color.GREEN)));
    //    getManagedStyles().addLineStyle(new LineGutterBackground(3, new ConstColor(Color.GREEN)));
    //    getManagedStyles().addLineStyle(new LineSideIcon(100, new
    // ColorDrawable(Color.parseColor("#700170"))));

    for (int i = 0; i < tokens.tokens.size(); i++) {
      final var token = tokens.tokens.get(i);
      final int type = token.getType();
      final int offset = token.getStartIndex();
      final String text = token.getText();
      switch (type) {
        case HTMLLexer.LPAREN:
        case HTMLLexer.RPAREN:
        case HTMLLexer.LBRACK:
        case HTMLLexer.RBRACK:
        case HTMLLexer.LBRACE:
        case HTMLLexer.RBRACE:
        case HTMLLexer.SEMI:
        case HTMLLexer.COMMA:
        case HTMLLexer.ASSIGN:
        case HTMLLexer.BANG:
        case HTMLLexer.TILDE:
        case HTMLLexer.QUESTION:
        case HTMLLexer.COLON:
        case HTMLLexer.EQUAL:
        case HTMLLexer.GE:
        case HTMLLexer.LE:
        case HTMLLexer.NOTEQUAL:
        case HTMLLexer.AND:
        case HTMLLexer.OR:
        case HTMLLexer.INC:
        case HTMLLexer.DEC:
        case HTMLLexer.ADD:
        case HTMLLexer.SUB:
        case HTMLLexer.MUL:
        case HTMLLexer.BITAND:
        case HTMLLexer.BITOR:
        case HTMLLexer.CARET:
        case HTMLLexer.MOD:
        case HTMLLexer.ADD_ASSIGN:
        case HTMLLexer.SUB_ASSIGN:
        case HTMLLexer.MUL_ASSIGN:
        case HTMLLexer.DIV_ASSIGN:
        case HTMLLexer.AND_ASSIGN:
        case HTMLLexer.OR_ASSIGN:
        case HTMLLexer.XOR_ASSIGN:
        case HTMLLexer.MOD_ASSIGN:
        case HTMLLexer.LSHIFT_ASSIGN:
        case HTMLLexer.RSHIFT_ASSIGN:
        case HTMLLexer.URSHIFT_ASSIGN:
        case HTMLLexer.ARROW:
        case HTMLLexer.COLONCOLON:
        case HTMLLexer.ELLIPSIS:
        case HTMLLexer.DOT:
        case HTMLLexer.DOLLAR:
        case HTMLLexer.DIV:
        case HTMLLexer.AT:
        case HTMLLexer.CSSDOMATTR:
          spans.add(Span.obtain(offset, OPERATOR));
          break;

        case HTMLLexer.HtmlTags:
        case HTMLLexer.HtmlTagOne:
          spans.add(Span.obtain(offset, HTML_TAG));
          break;

        case HTMLLexer.HtmlAttr:
          spans.add(Span.obtain(offset, GhostColorScheme.ATTRIBUTE_NAME));
          break;

        case HTMLLexer.BLOCK_COMMENT:
        case HTMLLexer.LINE_COMMENT:
          spans.add(Span.obtain(offset, COMMENT));
          break;

        case HTMLLexer.STRING:
        case HTMLLexer.CHATREF:
          int currentLine = tokens.state.lineNumber;
          String value = text.substring(1, text.length() - 1);
          //          loadImageToLine(value, currentLine);
          spans.add(Span.obtain(offset, LITERAL));
          break;

        case HTMLLexer.DECIMAL_LITERAL:
        case HTMLLexer.OCT_LITERAL:
        case HTMLLexer.BINARY_LITERAL:
        case HTMLLexer.FLOAT_LITERAL:
        case HTMLLexer.HEX_FLOAT_LITERAL:
        case HTMLLexer.BOOL_LITERAL:
        case HTMLLexer.NULL_LITERAL:
        case HTMLLexer.HEX_LITERAL:
          spans.add(Span.obtain(offset, LITERAL));
          break;

        case HTMLLexer.LT:
        case HTMLLexer.GT:
        case HTMLLexer.OPEN_SLASH:
        case HTMLLexer.SLASH_CLOSE:
          spans.add(Span.obtain(offset, OPERATOR));
          break;
        case HTMLLexer.CSSKEYWORD:
          spans.add(Span.obtain(offset, KEYWORD));
          break;
        case HTMLLexer.LinkLiteral:
          spans.add(Span.obtain(offset, LITERAL));
          break;
        case HTMLLexer.IDENTIFIER:
          int color = TEXT_NORMAL;
          if (pretoken == HTMLLexer.LT
              || pretoken == HTMLLexer.GT
              || pretoken == HTMLLexer.SLASH_CLOSE
              || pretoken == HTMLLexer.OPEN_SLASH
              || pretoken == HTMLLexer.DOT) {
            color = HTML_TAG;
          } else if (pretoken == HTMLLexer.COLON) {
            color = IDENTIFIER_NAME;
          } else {
            color = TEXT_NORMAL;
          }

          spans.add(Span.obtain(offset, color));
          break;
        default:
          spans.add(Span.obtain(offset, TEXT_NORMAL));
          break;
      }

      if (type != HTMLLexer.WS) {
        pretoken = type;
      }
    }

    return spans;
  }

  @Override
  protected boolean isCodeBlockStart(IncrementalToken token) {
    int type = token.getType();
    return type == HTMLLexer.LT;
  }

  @Override
  protected boolean isCodeBlockEnd(IncrementalToken token) {
    int type = token.getType();
    return type == HTMLLexer.OPEN_SLASH;
  }
}
