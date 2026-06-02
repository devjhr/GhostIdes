package ir.hanzodev1375.ghostide.codeeditors.langs.html;

import androidx.annotation.NonNull;
import io.github.rosemoe.sora.lang.completion.CompletionItem;
import io.github.rosemoe.sora.lang.completion.CompletionItemKind;
import io.github.rosemoe.sora.lang.completion.SimpleCompletionIconDrawer;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.widget.CodeEditor;

public class CssCompletionItem extends CompletionItem {

  private final String insertText;
  private final String prefix;

  public CssCompletionItem(CharSequence label, CharSequence desc, String prefix) {
    super(label, desc);
    this.insertText = label.toString();
    this.prefix = prefix != null ? prefix : "";
  }

  @Override
  public CssCompletionItem kind(CompletionItemKind kind) {
    super.kind(kind);
    if (this.icon == null) {
      icon = SimpleCompletionIconDrawer.draw(kind);
    }
    return this;
  }

  @Override
  public void performCompletion(
      @NonNull CodeEditor editor, @NonNull Content text, int line, int column) {
    int start = column - prefix.length();
    if (start < 0) start = column;
    text.replace(line, start, line, column, insertText + ":");
    editor.setSelection(line, start + insertText.length() + 1);
  }
}
