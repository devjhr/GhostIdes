package ir.hanzodev1375.ghostide.codeeditors.langs.html;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult;
import io.github.rosemoe.sora.lang.completion.IdentifierAutoComplete;
import io.github.rosemoe.sora.lang.styling.Span;
import ir.hanzodev1375.ghostide.codeeditors.colorscheme.GhostColorScheme;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.CodeAnalyzer;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.IncrementalToken;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.LineState;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import com.caverock.androidsvg.SVG;
import android.graphics.drawable.PictureDrawable;
import io.github.rosemoe.sora.lang.styling.line.LineSideIcon;
import java.util.ArrayList;
import java.util.List;

import static ir.hanzodev1375.ghostide.codeeditors.colorscheme.GhostColorScheme.*;

public class HTMLAnalyzer extends CodeAnalyzer {
  private Context context;
  private String jsonFilePath;

  public HTMLAnalyzer() {
    super(HTMLLexer.class);
  }

  public void init(Context context, String jsonFilePath) {
    this.context = context;
    this.jsonFilePath = jsonFilePath;
  }

  @Override
  protected int[][] getMultilineTokenStartEndTypes() {

    return new int[][] {new int[] {-1}, new int[] {-1}};
  }

  @Override
  protected void handleIncompleteToken(IncrementalToken token) {

    token.type = HTMLLexer.IDENTIFIER;
  }

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
        case HTMLLexer.ABSTRACT:
        case HTMLLexer.ASSERT:
        case HTMLLexer.BREAK:
        case HTMLLexer.CASE:
        case HTMLLexer.CATCH:
        case HTMLLexer.CLASS:
        case HTMLLexer.FUNCTION:
        case HTMLLexer.CONST:
        case HTMLLexer.CONTINUE:
        case HTMLLexer.DEFAULT:
        case HTMLLexer.DO:
        case HTMLLexer.ELSE:
        case HTMLLexer.EXTENDS:
        case HTMLLexer.FINAL:
        case HTMLLexer.FINALLY:
        case HTMLLexer.FOR:
        case HTMLLexer.IF:
        case HTMLLexer.GOTO:
        case HTMLLexer.IMPLEMENTS:
        case HTMLLexer.IMPORT:
        case HTMLLexer.INSTANCEOF:
        case HTMLLexer.INTERFACE:
        case HTMLLexer.NATIVE:
        case HTMLLexer.NEW:
        case HTMLLexer.PACKAGE:
        case HTMLLexer.PRIVATE:
        case HTMLLexer.PROTECTED:
        case HTMLLexer.PUBLIC:
        case HTMLLexer.RETURN:
        case HTMLLexer.STATIC:
        case HTMLLexer.STRICTFP:
        case HTMLLexer.SUPER:
        case HTMLLexer.SWITCH:
        case HTMLLexer.SYNCHRONIZED:
        case HTMLLexer.THIS:
        case HTMLLexer.THROW:
        case HTMLLexer.THROWS:
        case HTMLLexer.TRANSIENT:
        case HTMLLexer.TRY:
        case HTMLLexer.VOID:
        case HTMLLexer.VOLATILE:
        case HTMLLexer.WHILE:
        case HTMLLexer.VAR:
        case HTMLLexer.LET:
        case HTMLLexer.DEBUGGER:
        case HTMLLexer.YELD:
        case HTMLLexer.BYTE:
        case HTMLLexer.CHAR:
        case HTMLLexer.DOUBLE:
        case HTMLLexer.ENUM:
        case HTMLLexer.FLOAT:
        case HTMLLexer.INT:
        case HTMLLexer.LONG:
        case HTMLLexer.SHORT:
        case HTMLLexer.BOOLEAN:
          spans.add(Span.obtain(offset, KEYWORD));
          break;

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
          loadImageToLine(value, currentLine);
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
          if (pretoken == HTMLLexer.LT) {
            color = HTML_TAG;
          } else if (pretoken == HTMLLexer.AT) {
            color = GhostColorScheme.COLORNEXTCHAR;
          } else if (pretoken == HTMLLexer.DOT) {
            color = GhostColorScheme.COLORNEXTDOT;
          } else if (pretoken == HTMLLexer.CLASS
              || pretoken == HTMLLexer.ABSTRACT
              || pretoken == HTMLLexer.FUNCTION) {
            color = COLORNEXTCHAR;
          } else if (pretoken == HTMLLexer.LET
              || pretoken == HTMLLexer.VAR
              || pretoken == HTMLLexer.CASE
              || pretoken == HTMLLexer.INSTANCEOF
              || pretoken == HTMLLexer.NEW
              || pretoken == HTMLLexer.BOOLEAN) {
            color = COLORNEXTLESS;
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
  protected int[] getCodeBlockTokens() {
    return new int[] {HTMLLexer.LBRACE, HTMLLexer.RBRACE};
  }

  @Override
  protected boolean isIdentifierToken(int tokenType) {
    return tokenType == HTMLLexer.IDENTIFIER;
  }

  public IdentifierAutoComplete.SyncIdentifiers getSyncIdentifiers() {
    return syncIdentifiers;
  }

  @Override
  protected boolean isCodeBlockStart(IncrementalToken token) {
    int type = token.getType();
    return type == HTMLLexer.LT || type == HTMLLexer.LBRACE;
  }

  @Override
  protected boolean isCodeBlockEnd(IncrementalToken token) {
    int type = token.getType();
    return type == HTMLLexer.OPEN_SLASH || type == HTMLLexer.RBRACE;
  }

  private boolean isImagePath(String path) {
    String lower = path.toLowerCase();
    return lower.endsWith(".png")
        || lower.endsWith(".jpg")
        || lower.endsWith(".jpeg")
        || lower.endsWith(".webp")
        || lower.endsWith(".gif")
        || lower.endsWith(".svg");
  }

  private void loadSvgToLine(String value, int currentLine) {
    if (value.startsWith("./")) {
      value = value.substring(2);
    }
    File file = new File(value);
    if (!file.isAbsolute()) {
      File parent = new File(jsonFilePath).getParentFile();
      if (parent != null) {
        file = new File(parent, value);
      }
    }
    try {
      file = file.getCanonicalFile();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    if (!file.exists()) return;
    try (FileInputStream fis = new FileInputStream(file)) {
      SVG svg = SVG.getFromInputStream(fis);
      svg.setDocumentWidth(48);
      svg.setDocumentHeight(48);
      PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());
      if (getManagedStyles() != null) {
        getManagedStyles().eraseLineStyle(currentLine, LineSideIcon.class);
        getManagedStyles().addLineStyle(new LineSideIcon(currentLine, drawable));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadImageToLine(String value, int currentLine) {
    if (value.toLowerCase().endsWith(".svg")) {
      loadSvgToLine(value, currentLine);
      return;
    }
    if (!isImagePath(value)) {
      if (getManagedStyles() != null) {
        getManagedStyles().eraseLineStyle(currentLine, LineSideIcon.class);
      }
      return;
    }
    if (value.startsWith("./")) {
      value = value.substring(2);
    }
    File file = new File(value);
    if (!file.isAbsolute()) {
      File parent = new File(jsonFilePath).getParentFile();
      if (parent != null) {
        file = new File(parent, value);
      }
    }
    try {
      file = file.getCanonicalFile();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    if (!file.exists()) {
      if (getManagedStyles() != null) {
        getManagedStyles().eraseLineStyle(currentLine, LineSideIcon.class);
      }
      return;
    }
    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
    if (bitmap == null) {
      if (getManagedStyles() != null) {
        getManagedStyles().eraseLineStyle(currentLine, LineSideIcon.class);
      }
      return;
    }
    Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
    if (getManagedStyles() != null) {
      getManagedStyles().eraseLineStyle(currentLine, LineSideIcon.class);
      getManagedStyles().addLineStyle(new LineSideIcon(currentLine, drawable));
    }
  }
}
