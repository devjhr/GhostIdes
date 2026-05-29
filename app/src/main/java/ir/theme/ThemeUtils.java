package ir.theme;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import ir.hanzodev1375.ghostide.codeeditors.IdeEditor;
import java.lang.reflect.Field;

public class ThemeUtils {

  private final ThemeManager manager;

  public ThemeUtils(ThemeManager manager) {
    this.manager = manager;
  }

  public GhostTheme getTheme() {
    return manager.getTheme();
  }

  public void applyActivity(AppCompatActivity activity) {

    GhostTheme theme = getTheme();
    if (theme == null) {
      return;
    }

    if (theme.getActivity() == null) {
      return;
    }

    ActivityTheme colors = theme.getActivity();

    Window window = activity.getWindow();

    if (colors.getStatusBar() != null) {

      window.setStatusBarColor(parseColor(colors.getStatusBar()));
    }

    if (colors.getNavigationBar() != null) {

      window.setNavigationBarColor(parseColor(colors.getNavigationBar()));
    }

    if (colors.getBackground() != null) {

      View decor = window.getDecorView();

      decor.setBackgroundColor(parseColor(colors.getBackground()));
    }
  }

  public void applyEditor(IdeEditor editor) {

    GhostTheme theme = getTheme();

    if (theme == null) {
      return;
    }

    if (theme.getEditor() == null) {
      return;
    }

    EditorTheme t = theme.getEditor();

    EditorColorScheme scheme = editor.getColorScheme();
    scheme.setColor(EditorColorScheme.KEYWORD, Color.parseColor(t.getKeyword()));
    scheme.setColor(EditorColorScheme.WHOLE_BACKGROUND, Color.parseColor(t.getWholeBackground()));
    scheme.setColor(EditorColorScheme.LINE_DIVIDER, Color.parseColor(t.getLineDivider()));
    scheme.setColor(EditorColorScheme.LINE_NUMBER, Color.parseColor(t.getLineNumber()));
    scheme.setColor(
        EditorColorScheme.LINE_NUMBER_BACKGROUND, Color.parseColor(t.getLineNumberBackground()));
    scheme.setColor(EditorColorScheme.WHOLE_BACKGROUND, Color.parseColor(t.getWholeBackground()));
    scheme.setColor(EditorColorScheme.TEXT_NORMAL, Color.parseColor(t.getTextNormal()));
    scheme.setColor(
        EditorColorScheme.SELECTED_TEXT_BACKGROUND,
        Color.parseColor(t.getSelectedTextBackground()));
    scheme.setColor(EditorColorScheme.SELECTION_INSERT, Color.parseColor(t.getSelectionInsert()));
    scheme.setColor(EditorColorScheme.SELECTION_HANDLE, Color.parseColor(t.getSelectionHandle()));
    scheme.setColor(EditorColorScheme.CURRENT_LINE, Color.parseColor(t.getCurrentLine()));
    scheme.setColor(EditorColorScheme.UNDERLINE, Color.parseColor(t.getUnderline()));
    scheme.setColor(EditorColorScheme.SCROLL_BAR_THUMB, Color.parseColor(t.getScrollBarThumb()));
    scheme.setColor(
        EditorColorScheme.SCROLL_BAR_THUMB_PRESSED, Color.parseColor(t.getScrollBarThumbPressed()));
    scheme.setColor(EditorColorScheme.SCROLL_BAR_TRACK, Color.parseColor(t.getScrollBarTrack()));
    scheme.setColor(EditorColorScheme.BLOCK_LINE, Color.parseColor(t.getBlockLine()));
    scheme.setColor(
        EditorColorScheme.BLOCK_LINE_CURRENT, Color.parseColor(t.getBlockLineCurrent()));
    scheme.setColor(EditorColorScheme.LINE_NUMBER_PANEL, Color.parseColor(t.getLineNumberPanel()));
    scheme.setColor(
        EditorColorScheme.LINE_NUMBER_PANEL_TEXT, Color.parseColor(t.getLineNumberPanelText()));
    scheme.setColor(
        EditorColorScheme.COMPLETION_WND_BACKGROUND,
        Color.parseColor(t.getCompletionWndBackground()));
    scheme.setColor(
        EditorColorScheme.COMPLETION_WND_CORNER, Color.parseColor(t.getCompletionWndCorner()));
    scheme.setColor(EditorColorScheme.KEYWORD, Color.parseColor(t.getKeyword()));
    scheme.setColor(EditorColorScheme.COMMENT, Color.parseColor(t.getComment()));
    scheme.setColor(EditorColorScheme.OPERATOR, Color.parseColor(t.getOperator()));
    scheme.setColor(EditorColorScheme.LITERAL, Color.parseColor(t.getLiteral()));
    scheme.setColor(EditorColorScheme.IDENTIFIER_VAR, Color.parseColor(t.getIdentifierVar()));
    scheme.setColor(EditorColorScheme.IDENTIFIER_NAME, Color.parseColor(t.getIdentifierName()));
    scheme.setColor(EditorColorScheme.FUNCTION_NAME, Color.parseColor(t.getFunctionName()));
    scheme.setColor(EditorColorScheme.ANNOTATION, Color.parseColor(t.getAnnotation()));
    scheme.setColor(
        EditorColorScheme.MATCHED_TEXT_BACKGROUND, Color.parseColor(t.getMatchedTextBackground()));
    scheme.setColor(EditorColorScheme.TEXT_SELECTED, Color.parseColor(t.getTextSelected()));
    scheme.setColor(
        EditorColorScheme.NON_PRINTABLE_CHAR, Color.parseColor(t.getNonPrintableChar()));
    scheme.setColor(EditorColorScheme.HTML_TAG, Color.parseColor(t.getHtmlTag()));
    scheme.setColor(EditorColorScheme.ATTRIBUTE_NAME, Color.parseColor(t.getAttributeName()));
    scheme.setColor(EditorColorScheme.ATTRIBUTE_VALUE, Color.parseColor(t.getAttributeValue()));
    scheme.setColor(EditorColorScheme.PROBLEM_ERROR, Color.parseColor(t.getProblemError()));
    scheme.setColor(EditorColorScheme.PROBLEM_WARNING, Color.parseColor(t.getProblemWarning()));
    scheme.setColor(EditorColorScheme.PROBLEM_TYPO, Color.parseColor(t.getProblemTypo()));
    //
    //    setColor(scheme, EditorColorScheme.LINE_DIVIDER, t.getLineDivider());
    //
    //    setColor(scheme, EditorColorScheme.LINE_NUMBER, t.getLineNumber());
    //
    //    setColor(scheme, EditorColorScheme.LINE_NUMBER_BACKGROUND, t.getLineNumberBackground());
    //
    //    setColor(scheme, EditorColorScheme.WHOLE_BACKGROUND, t.getWholeBackground());
    //
    //    setColor(scheme, EditorColorScheme.TEXT_NORMAL, t.getTextNormal());
    //
    //    setColor(scheme, EditorColorScheme.SELECTED_TEXT_BACKGROUND,
    // t.getSelectedTextBackground());
    //
    //    setColor(scheme, EditorColorScheme.SELECTION_INSERT, t.getSelectionInsert());
    //
    //    setColor(scheme, EditorColorScheme.SELECTION_HANDLE, t.getSelectionHandle());
    //
    //    setColor(scheme, EditorColorScheme.CURRENT_LINE, t.getCurrentLine());
    //
    //    setColor(scheme, EditorColorScheme.UNDERLINE, t.getUnderline());
    //
    //    setColor(scheme, EditorColorScheme.SCROLL_BAR_THUMB, t.getScrollBarThumb());
    //
    //    setColor(scheme, EditorColorScheme.SCROLL_BAR_THUMB_PRESSED,
    // t.getScrollBarThumbPressed());
    //
    //    setColor(scheme, EditorColorScheme.SCROLL_BAR_TRACK, t.getScrollBarTrack());
    //
    //    setColor(scheme, EditorColorScheme.BLOCK_LINE, t.getBlockLine());
    //
    //    setColor(scheme, EditorColorScheme.BLOCK_LINE_CURRENT, t.getBlockLineCurrent());
    //
    //    setColor(scheme, EditorColorScheme.LINE_NUMBER_PANEL, t.getLineNumberPanel());
    //
    //    setColor(scheme, EditorColorScheme.LINE_NUMBER_PANEL_TEXT, t.getLineNumberPanelText());
    //
    //    setColor(scheme, EditorColorScheme.COMPLETION_WND_BACKGROUND,
    // t.getCompletionWndBackground());
    //
    //    setColor(scheme, EditorColorScheme.COMPLETION_WND_CORNER, t.getCompletionWndCorner());
    //
    //    setColor(scheme, EditorColorScheme.KEYWORD, t.getKeyword());
    //
    //    setColor(scheme, EditorColorScheme.COMMENT, t.getComment());
    //
    //    setColor(scheme, EditorColorScheme.OPERATOR, t.getOperator());
    //
    //    setColor(scheme, EditorColorScheme.LITERAL, t.getLiteral());
    //
    //    setColor(scheme, EditorColorScheme.IDENTIFIER_VAR, t.getIdentifierVar());
    //
    //    setColor(scheme, EditorColorScheme.IDENTIFIER_NAME, t.getIdentifierName());
    //
    //    setColor(scheme, EditorColorScheme.FUNCTION_NAME, t.getFunctionName());
    //
    //    setColor(scheme, EditorColorScheme.ANNOTATION, t.getAnnotation());
    //
    //    setColor(scheme, EditorColorScheme.MATCHED_TEXT_BACKGROUND, t.getMatchedTextBackground());
    //
    //    setColor(scheme, EditorColorScheme.TEXT_SELECTED, t.getTextSelected());
    //
    //    setColor(scheme, EditorColorScheme.NON_PRINTABLE_CHAR, t.getNonPrintableChar());
    //
    //    setColor(scheme, EditorColorScheme.HTML_TAG, t.getHtmlTag());
    //
    //    setColor(scheme, EditorColorScheme.ATTRIBUTE_NAME, t.getAttributeName());
    //
    //    setColor(scheme, EditorColorScheme.ATTRIBUTE_VALUE, t.getAttributeValue());
    //
    //    setColor(scheme, EditorColorScheme.PROBLEM_ERROR, t.getProblemError());
    //
    //    setColor(scheme, EditorColorScheme.PROBLEM_WARNING, t.getProblemWarning());
    //
    //    setColor(scheme, EditorColorScheme.PROBLEM_TYPO, t.getProblemTypo());

    //   editor.invalidate();
  }

  public void applyTextView(TextView textView) {

    GhostTheme theme = getTheme();

    if (theme == null) {
      return;
    }

    if (theme.getWidget() == null) {
      return;
    }

    WidgetTheme widget = theme.getWidget();

    if (widget.getText() != null) {

      textView.setTextColor(parseColor(widget.getText()));
    }

    if (widget.getHint() != null) {

      textView.setHintTextColor(parseColor(widget.getHint()));
    }
  }

  public void applyImageView(ImageView imageView) {

    GhostTheme theme = getTheme();

    if (theme == null) {
      return;
    }

    if (theme.getWidget() == null) {
      return;
    }

    WidgetTheme widget = theme.getWidget();

    if (widget.getImageTint() == null) {
      return;
    }

    imageView.setColorFilter(parseColor(widget.getImageTint()));
  }

  public void applyFab(FloatingActionButton fab) {

    GhostTheme theme = getTheme();

    if (theme == null) {
      return;
    }

    if (theme.getWidget() == null) {
      return;
    }

    WidgetTheme widget = theme.getWidget();

    if (widget.getFabBackground() != null) {

      fab.setBackgroundTintList(ColorStateList.valueOf(parseColor(widget.getFabBackground())));
    }

    if (widget.getFabIcon() != null) {

      fab.setColorFilter(parseColor(widget.getFabIcon()));
    }
  }

  public void applyTabLayout(TabLayout layout) {

    GhostTheme theme = getTheme();

    if (theme == null) {
      return;
    }

    if (theme.getWidget() == null) {
      return;
    }

    WidgetTheme widget = theme.getWidget();

    if (widget.getBackground() != null) {

      layout.setBackgroundColor(parseColor(widget.getBackground()));
    }

    if (widget.getAccent() != null) {

      layout.setSelectedTabIndicatorColor(parseColor(widget.getAccent()));
    }

    if (widget.getTabSelected() != null && widget.getTabUnselected() != null) {

      layout.setTabTextColors(
          parseColor(widget.getTabUnselected()), parseColor(widget.getTabSelected()));
    }
  }

  private int parseColor(String color) {

    try {

      return Color.parseColor(color);

    } catch (Exception e) {

      return Color.WHITE;
    }
  }

  private String toConstant(String camelCase) {

    StringBuilder builder = new StringBuilder();

    for (char c : camelCase.toCharArray()) {

      if (Character.isUpperCase(c)) {

        builder.append("_");
      }

      builder.append(Character.toUpperCase(c));
    }

    return builder.toString();
  }
}
