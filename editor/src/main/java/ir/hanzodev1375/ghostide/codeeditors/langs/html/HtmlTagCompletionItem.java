package ir.hanzodev1375.ghostide.codeeditors.langs.html;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import io.github.rosemoe.sora.lang.completion.CompletionItem;
import io.github.rosemoe.sora.lang.completion.CompletionItemKind;
import io.github.rosemoe.sora.lang.completion.SimpleCompletionIconDrawer;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.widget.CodeEditor;

public class HtmlTagCompletionItem extends CompletionItem {

  private final String tagName;
  private final boolean selfClosing;
  private final String prefix;

  public HtmlTagCompletionItem(String tagName, String desc) {
    this(tagName, desc, false, "");
  }

  public HtmlTagCompletionItem(String tagName, String desc, boolean selfClosing) {
    this(tagName, desc, selfClosing, "");
  }

  public HtmlTagCompletionItem(String tagName, String desc, boolean selfClosing, String prefix) {
    super(tagName, desc);
    this.tagName = tagName;
    this.selfClosing = selfClosing;
    this.prefix = (prefix != null) ? prefix : "";
    this.kind = CompletionItemKind.Keyword;
  }

  public void setKind(CompletionItemKind kind) {
    this.kind = kind;
  }

  public HtmlTagCompletionItem withIcon(Drawable icon) {
    this.icon = icon;
    return this;
  }

  public HtmlTagCompletionItem withDetail(String detail) {
    this.detail = detail;
    return this;
  }

  public boolean isSelfClosing() {
    return selfClosing;
  }

  @Override
  public HtmlTagCompletionItem kind(CompletionItemKind kind) {
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
    if (start >= 0 && start < column) {
      text.delete(line, start, line, column);
    } else {
      start = column;
    }

    if (selfClosing) {
      String insertText = "<" + tagName + "/>";
      text.insert(line, start, insertText);
      editor.setSelection(line, start + insertText.length());
    } else {
      String openTag = "<" + tagName + ">";
      String closeTag = "</" + tagName + ">";
      String insertText = openTag + closeTag;
      text.insert(line, start, insertText);
      editor.setSelection(line, start + openTag.length());
    }
  }
}
