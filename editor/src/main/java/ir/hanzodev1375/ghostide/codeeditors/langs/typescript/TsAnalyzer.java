package ir.hanzodev1375.ghostide.codeeditors.langs.typescript;

import io.github.rosemoe.sora.lang.completion.IdentifierAutoComplete;
import io.github.rosemoe.sora.lang.styling.Span;
import static ir.hanzodev1375.ghostide.codeeditors.colorscheme.GhostColorScheme.*;
import ir.hanzodev1375.ghostide.codeeditors.colorscheme.GhostColorScheme;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.CodeAnalyzer;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.IncrementalToken;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr4base.LineState;
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TsAnalyzer extends CodeAnalyzer {

  public TsAnalyzer() {
    super(TypeScriptLexer.class);
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
    return tokenType == TypeScriptLexer.Identifier;
  }

  @Override
  protected void handleIncompleteToken(IncrementalToken token) {}

  @Override
  protected List<Span> generateSpans(final LineTokenizeResult<LineState, IncrementalToken> tokens) {
    final List<Span> spans = new ArrayList<>();
    int pretoken = -1;
    for (int i = 0; i < tokens.tokens.size(); i++) {
      final var token = tokens.tokens.get(i);
      final int type = token.getType();
      final int offset = token.getStartIndex();
      final String text = token.getText();
      switch (type) {
        case TypeScriptLexer.CloseBrace:
        case TypeScriptLexer.OpenBrace:
        case TypeScriptLexer.RegularExpressionLiteral:
        case TypeScriptLexer.OpenBracket:
        case TypeScriptLexer.CloseBracket:
        case TypeScriptLexer.BitNot:
        case TypeScriptLexer.Not:
        case TypeScriptLexer.Multiply:
        case TypeScriptLexer.Divide:
        case TypeScriptLexer.Modulus:
        case TypeScriptLexer.RightShiftArithmetic:
        case TypeScriptLexer.LeftShiftArithmetic:
        case TypeScriptLexer.RightShiftLogical:
        case TypeScriptLexer.LessThan:
        case TypeScriptLexer.MoreThan:
        case TypeScriptLexer.LessThanEquals:
        case TypeScriptLexer.GreaterThanEquals:
        case TypeScriptLexer.Equals_:
        case TypeScriptLexer.NotEquals:
        case TypeScriptLexer.IdentityEquals:
        case TypeScriptLexer.IdentityNotEquals:
        case TypeScriptLexer.BitAnd:
        case TypeScriptLexer.BitXOr:
        case TypeScriptLexer.BitOr:
        case TypeScriptLexer.And:
        case TypeScriptLexer.Or:
        case TypeScriptLexer.MultiplyAssign:
        case TypeScriptLexer.DivideAssign:
        case TypeScriptLexer.ModulusAssign:
        case TypeScriptLexer.PlusAssign:
        case TypeScriptLexer.MinusAssign:
        case TypeScriptLexer.LeftShiftArithmeticAssign:
        case TypeScriptLexer.RightShiftArithmeticAssign:
        case TypeScriptLexer.RightShiftLogicalAssign:
        case TypeScriptLexer.BitAndAssign:
        case TypeScriptLexer.BitXorAssign:
        case TypeScriptLexer.BitOrAssign:
        case TypeScriptLexer.ARROW:
        case TypeScriptLexer.NullLiteral:
        case TypeScriptLexer.BooleanLiteral:
        case TypeScriptLexer.DecimalLiteral:
        case TypeScriptLexer.HexIntegerLiteral:
        case TypeScriptLexer.OctalIntegerLiteral:
        case TypeScriptLexer.BinaryIntegerLiteral:
        case TypeScriptLexer.At:
        case TypeScriptLexer.Colon:
        case TypeScriptLexer.TemplateCloseBrace:
        case TypeScriptLexer.OpenParen:
        case TypeScriptLexer.SemiColon:
        case TypeScriptLexer.Comma:
        case TypeScriptLexer.Assign:
        case TypeScriptLexer.QuestionMark:
        case TypeScriptLexer.Ellipsis:
        case TypeScriptLexer.Dot:
        case TypeScriptLexer.PlusPlus:
        case TypeScriptLexer.MinusMinus:
        case TypeScriptLexer.Plus:
        case TypeScriptLexer.CloseParen:
        case TypeScriptLexer.Minus:
          spans.add(Span.obtain(offset, OPERATOR));
          break;

        case TypeScriptLexer.Break:
        case TypeScriptLexer.Do:
        case TypeScriptLexer.Instanceof:
        case TypeScriptLexer.Typeof:
        case TypeScriptLexer.Case:
        case TypeScriptLexer.Else:
        case TypeScriptLexer.New:
        case TypeScriptLexer.Var:
        case TypeScriptLexer.Catch:
        case TypeScriptLexer.Finally:
        case TypeScriptLexer.Return:
        case TypeScriptLexer.Void:
        case TypeScriptLexer.Continue:
        case TypeScriptLexer.For:
        case TypeScriptLexer.Switch:
        case TypeScriptLexer.While:
        case TypeScriptLexer.Debugger:
        case TypeScriptLexer.Function_:
        case TypeScriptLexer.This:
        case TypeScriptLexer.With:
        case TypeScriptLexer.Default:
        case TypeScriptLexer.If:
        case TypeScriptLexer.Throw:
        case TypeScriptLexer.Delete:
        case TypeScriptLexer.In:
        case TypeScriptLexer.Try:
        case TypeScriptLexer.As:
        case TypeScriptLexer.From:
        case TypeScriptLexer.ReadOnly:
        case TypeScriptLexer.Async:
        case TypeScriptLexer.Class:
        case TypeScriptLexer.Enum:
        case TypeScriptLexer.Extends:
        case TypeScriptLexer.Super:
        case TypeScriptLexer.Const:
        case TypeScriptLexer.Export:
        case TypeScriptLexer.Import:
        case TypeScriptLexer.Implements:
        case TypeScriptLexer.Let:
        case TypeScriptLexer.Private:
        case TypeScriptLexer.Public:
        case TypeScriptLexer.Interface:
        case TypeScriptLexer.Package:
        case TypeScriptLexer.Protected:
        case TypeScriptLexer.Static:
        case TypeScriptLexer.Yield:
        case TypeScriptLexer.Any:
          spans.add(Span.obtain(offset, KEYWORD));
          break;

        case TypeScriptLexer.Number:
        case TypeScriptLexer.Boolean:
        case TypeScriptLexer.String:
        case TypeScriptLexer.Symbol:
        case TypeScriptLexer.TypeAlias:
        case TypeScriptLexer.Get:
        case TypeScriptLexer.Set:
        case TypeScriptLexer.Constructor:
        case TypeScriptLexer.Namespace:
        case TypeScriptLexer.Require:
        case TypeScriptLexer.Module:
        case TypeScriptLexer.Declare:
        case TypeScriptLexer.Abstract:
        case TypeScriptLexer.Is:
        case TypeScriptLexer.BackTick:
        case TypeScriptLexer.LineTerminator:
        case TypeScriptLexer.UnexpectedCharacter:
        case TypeScriptLexer.TemplateStringEscapeAtom:
        case TypeScriptLexer.TemplateStringStartExpression:
        case TypeScriptLexer.TemplateStringAtom:
          spans.add(Span.obtain(offset, GhostColorScheme.ATTRIBUTE_NAME));
          break;

        case TypeScriptLexer.MultiLineComment:
        case TypeScriptLexer.SingleLineComment:
        case TypeScriptLexer.HtmlComment:
        case TypeScriptLexer.CDataComment:
          spans.add(Span.obtain(offset, COMMENT));
          break;
        case TypeScriptLexer.Identifier:
          {
            int colorNormal = TEXT_NORMAL;
            boolean isClassName = false, isbold = false, Varunderline = false;
            if (type == TypeScriptLexer.Class
                || type == TypeScriptLexer.Interface
                || type == TypeScriptLexer.Enum
                || type == TypeScriptLexer.Extends
                || type == TypeScriptLexer.Function_
                || type == TypeScriptLexer.Implements) {
              colorNormal = GhostColorScheme.IDENTIFIER_NAME;
              isbold = true;
              isClassName = true;
            } else if (type == TypeScriptLexer.Void
                || type == TypeScriptLexer.Boolean
                || type == TypeScriptLexer.Any
                || type == TypeScriptLexer.Const
                || type == TypeScriptLexer.Async
                || type == TypeScriptLexer.Instanceof
                || type == TypeScriptLexer.Let
                || type == TypeScriptLexer.Not
                || type == TypeScriptLexer.Var
                || type == TypeScriptLexer.Abstract
                || type == TypeScriptLexer.Identifier) {
              Varunderline = true;

              colorNormal = GhostColorScheme.FUNCTION_NAME;
              isbold = true;
              if (lexer._input.LA(1) == '(') {
                colorNormal = GhostColorScheme.ATTRIBUTE_NAME;
              }
            } else if (lexer._input.LA(1) == '.') {
              colorNormal = GhostColorScheme.ATTRIBUTE_NAME;
            } else if (lexer._input.LA(1) == '[' || lexer._input.LA(1) == ']') {
              colorNormal = GhostColorScheme.ATTRIBUTE_VALUE;
            } else if (type == TypeScriptLexer.Dot) {
              colorNormal = GhostColorScheme.COLORNEXTDOT;
            } else if (!isClassName && Character.isUpperCase(token.getText().charAt(0))) {
              Pattern pattern = Pattern.compile("^[A-Z][a-zA-Z0-9_]*$");
              var matcher = pattern.matcher(token.getText());
              if (matcher.matches()) {
                colorNormal = GhostColorScheme.COLORUPPERCASE;
              }
            }

            spans.add(Span.obtain(offset, colorNormal));
            break;
          }
        case TypeScriptLexer.StringLiteral:
          spans.add(Span.obtain(offset, LITERAL));
          break;
        default:
          spans.add(Span.obtain(offset, TEXT_NORMAL));
          break;
      }

      if (type != TypeScriptLexer.WhiteSpaces) {
        pretoken = type;
      }
    }

    return spans;
  }

  @Override
  protected boolean isCodeBlockStart(IncrementalToken token) {
    int type = token.getType();
    return type == TypeScriptLexer.OpenBrace;
  }

  @Override
  protected boolean isCodeBlockEnd(IncrementalToken token) {
    int type = token.getType();
    return type == TypeScriptLexer.CloseBrace;
  }

  public IdentifierAutoComplete.SyncIdentifiers getSyncIdentifiers() {
    return syncIdentifiers;
  }
}
