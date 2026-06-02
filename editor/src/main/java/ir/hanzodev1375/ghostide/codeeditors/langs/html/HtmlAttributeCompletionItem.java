package ir.hanzodev1375.ghostide.codeeditors.langs.html;

import androidx.annotation.NonNull;
import io.github.rosemoe.sora.lang.completion.CompletionItem;
import io.github.rosemoe.sora.lang.completion.CompletionItemKind;
import io.github.rosemoe.sora.lang.completion.SimpleCompletionIconDrawer;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.widget.CodeEditor;

public class HtmlAttributeCompletionItem extends CompletionItem {

  private final String attrName;
  private final String prefix;

  public HtmlAttributeCompletionItem(String attrName, String description) {
    this(attrName, description, "");
  }

  public HtmlAttributeCompletionItem(String attrName, String description, String prefix) {
    super(attrName, description);
    this.attrName = attrName;
    this.prefix = (prefix != null) ? prefix : "";
    this.kind = CompletionItemKind.Property;
  }

  @Override
  public HtmlAttributeCompletionItem kind(CompletionItemKind kind) {
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

    // حذف پیشوند تایپ شده
    if (start >= 0 && start < column) {
      text.delete(line, start, line, column);
    } else {
      start = column;
    }

    // درج attributeName="" و قرار دادن cursor داخل کوتیشن‌ها
    String insertText = attrName + "=\"\"";
    text.insert(line, start, insertText);
    editor.setSelection(line, start + attrName.length() + 2);
  }
}
