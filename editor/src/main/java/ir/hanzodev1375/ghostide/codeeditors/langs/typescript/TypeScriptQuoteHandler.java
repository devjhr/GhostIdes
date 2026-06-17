package ir.hanzodev1375.ghostide.codeeditors.langs.typescript;

import static io.github.rosemoe.sora.lang.styling.StylesUtils.checkNoCompletion;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.github.rosemoe.sora.lang.QuickQuoteHandler;
import io.github.rosemoe.sora.lang.styling.Styles;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.TextRange;

public class TypeScriptQuoteHandler implements QuickQuoteHandler {

  @NonNull
  @Override
  public HandleResult onHandleTyping(
      @NonNull String candidateCharacter,
      @NonNull Content text,
      @NonNull TextRange cursor,
      @Nullable Styles style) {
    // فقط در صورتی که در ناحیه‌ای مجاز (غیر کامنت و غیر رشته) هستیم و انتخاب (selection) عادی است
    // (start==end)
    if (!checkNoCompletion(style, cursor.getStart())
        && !checkNoCompletion(style, cursor.getEnd())
        && cursor.getStart().line == cursor.getEnd().line) {
      char ch = candidateCharacter.charAt(0);
      // پشتیبانی از ", ', ` (تمپلیت)
      if (ch == '"' || ch == '\'' || ch == '`') {
        int line = cursor.getStart().line;
        int col = cursor.getStart().column;
        String lineText = text.getLine(line).toString();
        if (col < lineText.length() && lineText.charAt(col) == ch) {
          return new HandleResult(
              true,
              new TextRange(
                  text.getIndexer().getCharPosition(cursor.getStartIndex() + 1),
                  text.getIndexer().getCharPosition(cursor.getEndIndex() + 1)));
        } else {
          text.insert(line, col, candidateCharacter);
          text.insert(line, col + 1, candidateCharacter);
          // مکان‌نما را بین دو نقل‌قول قرار بده
          return new HandleResult(
              true,
              new TextRange(
                  text.getIndexer().getCharPosition(cursor.getStartIndex() + 1),
                  text.getIndexer().getCharPosition(cursor.getEndIndex() + 1)));
        }
      }
    }
    return HandleResult.NOT_CONSUMED;
  }
}
