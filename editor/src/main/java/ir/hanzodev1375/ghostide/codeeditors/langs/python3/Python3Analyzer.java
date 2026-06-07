package ir.hanzodev1375.ghostide.codeeditors.langs.python3;

import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult;
import io.github.rosemoe.sora.lang.completion.IdentifierAutoComplete;
import io.github.rosemoe.sora.lang.styling.Span;
import ir.hanzodev1375.ghostide.codeeditors.colorscheme.GhostColorScheme;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.CodeAnalyzer;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.IncrementalToken;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.LineState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ir.hanzodev1375.ghostide.codeeditors.colorscheme.GhostColorScheme.*;
import java.util.regex.Pattern;

public class Python3Analyzer extends CodeAnalyzer {

  public Python3Analyzer() {
    super(PythonLexerCompat.class);
  }

  @Override
  protected int[][] getMultilineTokenStartEndTypes() {
    return new int[][] {new int[] {-1}, new int[] {-1}};
  }

  @Override
  protected void handleIncompleteToken(IncrementalToken token) {
    token.type = PythonLexerCompat.IDENTIFIER;
  }

  @Override
  protected List<Span> generateSpans(final LineTokenizeResult<LineState, IncrementalToken> tokens) {
    final List<Span> spans = new ArrayList<>();
    int previous = -1;
    for (int i = 0; i < tokens.tokens.size(); i++) {
      final var token = tokens.tokens.get(i);
      int type = token.getType();
      final int offset = token.getStartIndex();
      final String text = token.getText();
      switch (type) {
        case PythonLexerCompat.AND:
        case PythonLexerCompat.AS:
        case PythonLexerCompat.ASYNC:
        case PythonLexerCompat.AWAIT:
        case PythonLexerCompat.BREAK:
        case PythonLexerCompat.CLASS:
        case PythonLexerCompat.CONTINUE:
        case PythonLexerCompat.DEF:
        case PythonLexerCompat.DEL:
        case PythonLexerCompat.ELIF:
        case PythonLexerCompat.ELSE:
        case PythonLexerCompat.EXCEPT:
        case PythonLexerCompat.FALSE:
        case PythonLexerCompat.FINALLY:
        case PythonLexerCompat.FOR:
        case PythonLexerCompat.GLOBAL:
        case PythonLexerCompat.IF:
        case PythonLexerCompat.IMPORT:
        case PythonLexerCompat.IN:
        case PythonLexerCompat.IS:
        case PythonLexerCompat.RAISE:
        case PythonLexerCompat.RETURN:
        case PythonLexerCompat.TRUE:
        case PythonLexerCompat.TRY:
        case PythonLexerCompat.WITH:
        case PythonLexerCompat.YIELD:
        case PythonLexerCompat.FROM:
        case PythonLexerCompat.WHILE:
        case PythonLexerCompat.ASSERT:
        case PythonLexerCompat.LAMBDA:
        case PythonLexerCompat.NONE:
        case PythonLexerCompat.NONLOCAL:
        case PythonLexerCompat.NOT:
        case PythonLexerCompat.OR:
        case PythonLexerCompat.PASS:
        case PythonLexerCompat.NAME_OR_TYPE:
        case PythonLexerCompat.NAME_OR_MATCH:
        case PythonLexerCompat.NAME_OR_CASE:
          spans.add(Span.obtain(offset, KEYWORD));

          break;
        case PythonLexerCompat.LBRACE:
        case PythonLexerCompat.LPAR:
        case PythonLexerCompat.LSQB:
        case PythonLexerCompat.RBRACE:
        case PythonLexerCompat.RPAR:
        case PythonLexerCompat.RSQB:
        case PythonLexerCompat.DOT:
        case PythonLexerCompat.STAR:
        case PythonLexerCompat.COMMA:
        case PythonLexerCompat.VBAR:
        case PythonLexerCompat.EQUAL:
        case PythonLexerCompat.PERCENT:
        case PythonLexerCompat.EQEQUAL:
        case PythonLexerCompat.NOTEQUAL:
        case PythonLexerCompat.LESSEQUAL:
        case PythonLexerCompat.GREATEREQUAL:
        case PythonLexerCompat.TILDE:
        case PythonLexerCompat.CIRCUMFLEX:
        case PythonLexerCompat.LEFTSHIFT:
        case PythonLexerCompat.RIGHTSHIFT:
        case PythonLexerCompat.DOUBLESTAR:
        case PythonLexerCompat.PLUSEQUAL:
        case PythonLexerCompat.MINEQUAL:
        case PythonLexerCompat.STAREQUAL:
        case PythonLexerCompat.SLASHEQUAL:
        case PythonLexerCompat.PERCENTEQUAL:
        case PythonLexerCompat.AMPEREQUAL:
        case PythonLexerCompat.VBAREQUAL:
        case PythonLexerCompat.CIRCUMFLEXEQUAL:
        case PythonLexerCompat.LEFTSHIFTEQUAL:
        case PythonLexerCompat.RIGHTSHIFTEQUAL:
        case PythonLexerCompat.DOUBLESTAREQUAL:
        case PythonLexerCompat.DOUBLESLASH:
        case PythonLexerCompat.DOUBLESLASHEQUAL:
        case PythonLexerCompat.AT:
        case PythonLexerCompat.ATEQUAL:
        case PythonLexerCompat.RARROW:
        case PythonLexerCompat.ELLIPSIS:
        case PythonLexerCompat.COLONEQUAL:
        case PythonLexerCompat.EXCLAMATION:
          spans.add(Span.obtain(offset, OPERATOR));
          break;

        case PythonLexerCompat.COMMENT:
          spans.add(Span.obtain(offset, COMMENT));
          break;
        case PythonLexerCompat.STRING:
        case PythonLexerCompat.NUMBER:
          spans.add(Span.obtain(offset, LITERAL));
          break;
        case PythonLexerCompat.IDENTIFIER:
          {
            int colorId = TEXT_NORMAL;
            boolean isbold = false;
            if (previous == PythonLexerCompat.CLASS || previous == PythonLexerCompat.DEF) {
              colorId = COLORNEXTCHAR;
              isbold = true;
            } else if (previous == PythonLexerCompat.FROM
                || previous == PythonLexerCompat.IMPORT
                || previous == PythonLexerCompat.AS) {
              colorId = COLORNEXTBRAK;
            } else if (previous == PythonLexerCompat.AT) {
              colorId = GhostColorScheme.OPERATOR;
            } else if (previous == PythonLexerCompat.RETURN
                || previous == PythonLexerCompat.YIELD) {
              colorId = GhostColorScheme.ATTRIBUTE_NAME;
            } else if (previous == PythonLexerCompat.IF
                || previous == PythonLexerCompat.ELIF
                || previous == PythonLexerCompat.WHILE
                || previous == PythonLexerCompat.FOR) {
              colorId = GhostColorScheme.FUNCTION_NAME;
            } else if (previous == PythonLexerCompat.IDENTIFIER) {
//              if (ObjectUtils.getNextLexer(lexer, '(')) {
//                colorId = EditorColorScheme.pycolormatch1;
//              }
            }

//            if (ObjectUtils.getNextLexer(lexer, '(')) {
//              colorId = EditorColorScheme.pycolormatch2;
//            } else if (ObjectUtils.getNextLexer(lexer, '.')) {
//              colorId = EditorColorScheme.pycolormatch3;
//            } else if (ObjectUtils.getNextLexer(lexer, '[')) {
//              colorId = EditorColorScheme.phpcolormatch4;
//            } else if (ObjectUtils.getNextLexer(lexer, ':')) {
//              colorId = EditorColorScheme.phpcolormatch6;
//            }

            Set<String> builtinFuncs =
                new HashSet<>(
                    Arrays.asList(
                        "add",
                        "append",
                        "as_integer_ratio",
                        "bit_count",
                        "bit_length",
                        "capitalize",
                        "casefold",
                        "center",
                        "clear",
                        "close",
                        "conjugate",
                        "copy",
                        "count",
                        "detach",
                        "difference",
                        "difference_update",
                        "discard",
                        "encode",
                        "endswith",
                        "expandtabs",
                        "extend",
                        "fileno",
                        "find",
                        "flush",
                        "format",
                        "format_map",
                        "from_bytes",
                        "fromkeys",
                        "get",
                        "index",
                        "insert",
                        "intersection",
                        "intersection_update",
                        "isalnum",
                        "isalpha",
                        "isascii",
                        "isatty",
                        "isdecimal",
                        "isdigit",
                        "isdisjoint",
                        "isidentifier",
                        "islower",
                        "isnumeric",
                        "isprintable",
                        "isspace",
                        "issubset",
                        "issuperset",
                        "istitle",
                        "isupper",
                        "items",
                        "join",
                        "keys",
                        "ljust",
                        "lower",
                        "lstrip",
                        "maketrans",
                        "partition",
                        "pop",
                        "popitem",
                        "read",
                        "readable",
                        "readline",
                        "readlines",
                        "reconfigure",
                        "remove",
                        "removeprefix",
                        "removesuffix",
                        "replace",
                        "reverse",
                        "rfind",
                        "rindex",
                        "rjust",
                        "rpartition",
                        "rsplit",
                        "rstrip",
                        "seek",
                        "seekable",
                        "setdefault",
                        "sort",
                        "split",
                        "splitlines",
                        "startswith",
                        "strip",
                        "swapcase",
                        "symmetric_difference",
                        "symmetric_difference_update",
                        "tell",
                        "title",
                        "to_bytes",
                        "translate",
                        "truncate",
                        "union",
                        "update",
                        "upper",
                        "values",
                        "writable",
                        "write",
                        "writelines",
                        "zfill"));
            if (builtinFuncs.contains(text)) {
              colorId = GhostColorScheme.COLORNEXTDOT;
            }

            if (Character.isUpperCase(text.charAt(0))) {
              Pattern pattern = Pattern.compile("^[A-Z][a-zA-Z0-9_]*$");
              if (pattern.matcher(text).matches()) {
                colorId = GhostColorScheme.COLORUPPERCASE;
              }
            }

            spans.add(Span.obtain(offset, colorId));
            break;
          }

        default:
          spans.add(Span.obtain(offset, TEXT_NORMAL));
          break;
      }
      if (type != PythonLexerCompat.WS) {
        previous = type;
      }
    }

    return spans;
  }

  @Override
  protected int[] getCodeBlockTokens() {
    return new int[] {PythonLexerCompat.LBRACE, PythonLexerCompat.RBRACE};
  }

  @Override
  protected boolean isIdentifierToken(int tokenType) {
    return tokenType == PythonLexerCompat.IDENTIFIER;
  }

  @Override
  protected boolean isCodeBlockStart(IncrementalToken token) {
    return false;
  }

  @Override
  protected boolean isCodeBlockEnd(IncrementalToken token) {
    return false;
  }

  public IdentifierAutoComplete.SyncIdentifiers getSyncIdentifiers() {
    return syncIdentifiers;
  }
}
