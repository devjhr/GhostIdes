package ir.hanzodev1375.ghostide.codeeditors.preview;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.base.EditorPopupWindow;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class ImagePreviewIde {

  private static final long PROCESS_DELAY_MS = 100;

  private final CodeEditor editor;
  private volatile String currentFilePath;
  private long lastProcessTime = 0;
  private String lastImagePath = "";
  private EditorPopupWindow activePopup;

  public ImagePreviewIde(CodeEditor editor) {
    this.editor = editor;
  }

  public void setCurrentFilePath(String htmlFilePath) {
    this.currentFilePath = htmlFilePath;
    lastImagePath = "";
    editor.post(this::dismissPopup);
  }

  public void attach() {
    editor.subscribeEvent(
        SelectionChangeEvent.class,
        (event, unsubscribe) -> {
          if (event.getCause() != SelectionChangeEvent.CAUSE_TAP) return;

          long now = System.currentTimeMillis();
          if (now - lastProcessTime < PROCESS_DELAY_MS) return;
          lastProcessTime = now;
          editor.post(
              () -> {
                try {
                  handleSelectionChange(event);
                } catch (Exception e) {
                  dismissPopup();
                }
              });
        });
  }

  private void handleSelectionChange(SelectionChangeEvent event) {
    if (!isHtmlFile() || editor.getCursor().isSelected()) {
      dismissPopup();
      lastImagePath = "";
      return;
    }

    int line = event.getLeft().getLine();
    int column = event.getLeft().getColumn();
    String lineText = editor.getText().getLineString(line);

    Match match = ImageRefUtils.findImagePathAtPosition(lineText, column);

    if (match == null) {
      dismissPopup();
      lastImagePath = "";
      return;
    }

    if (match.path.equals(lastImagePath) && activePopup != null && activePopup.isShowing()) {
      return;
    }

    File file = ImageRefUtils.resolve(currentFilePath, match.path);
    if (file == null || !file.exists() || !file.isFile()) {
      dismissPopup();
      lastImagePath = "";
      return;
    }

    lastImagePath = match.path;
    showPreview(file);
  }

  private boolean isHtmlFile() {
    if (currentFilePath == null) return false;
    String lower = currentFilePath.toLowerCase();
    return lower.endsWith(".html") || lower.endsWith(".htm");
  }

  private void showPreview(File file) {
    dismissPopup();
    activePopup = EditorPopUp.showCustomViewAtCursor(editor, buildPreviewView(file));
  }

  private View buildPreviewView(File file) {
    LinearLayout root = new LinearLayout(editor.getContext());
    root.setOrientation(LinearLayout.VERTICAL);
    root.setPadding(dp(12), dp(12), dp(12), dp(12));

    ImageView imageView = new ImageView(editor.getContext());
    LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dp(160), dp(160));
    imgParams.gravity = Gravity.CENTER_HORIZONTAL;
    imageView.setLayoutParams(imgParams);
    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    Glide.with(editor.getContext()).load(file).error(android.R.drawable.ic_delete).into(imageView);
    root.addView(imageView);

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    try {
      BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    } catch (Exception ignored) {
    }
    String dims =
        (options.outWidth > 0 && options.outHeight > 0)
            ? options.outWidth + " × " + options.outHeight + " px"
            : "نامشخص";

    root.addView(infoLine("سایز: " + dims));
    root.addView(infoLine("حجم: " + formatFileSize(file.length())));
    root.addView(infoLine("تاریخ: " + formatDate(file.lastModified())));
    root.addView(infoLine("مسیر: " + file.getAbsolutePath()));

    return root;
  }

  private TextView infoLine(String text) {
    TextView tv = new TextView(editor.getContext());
    tv.setText(text);
    tv.setTextSize(12);
    tv.setTextColor(Color.WHITE);
    tv.setPadding(0, dp(4), 0, 0);
    return tv;
  }

  private String formatFileSize(long bytes) {
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return String.format(Locale.US, "%.1f KB", bytes / 1024.0);
    return String.format(Locale.US, "%.2f MB", bytes / (1024.0 * 1024.0));
  }

  private String formatDate(long millis) {
    if (millis <= 0) return "نامشخص";
    return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date(millis));
  }

  private int dp(int value) {
    float density = editor.getContext().getResources().getDisplayMetrics().density;
    return (int) (value * density);
  }

  private void dismissPopup() {
    if (activePopup != null) {
      try {
        activePopup.dismiss();
      } catch (Exception ignored) {
      }
      activePopup = null;
    }
  }
}
