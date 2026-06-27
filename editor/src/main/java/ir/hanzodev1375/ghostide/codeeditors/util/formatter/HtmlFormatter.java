package ir.hanzodev1375.ghostide.codeeditors.util.formatter;

import io.beautifier.core.AndroidBeautify;
import io.beautifier.html.HTMLOptions;

public class HtmlFormatter {
  private static final HTMLOptions OPTIONS =
      HTMLOptions.builder()
          .indent_size(2)
          //  .indent_with_tabs(false)
          //  .wrap_line_length(0)
          .build();

  public String format(String text) {
    if (text == null || text.isEmpty()) {
      return text;
    }
    try {
      return AndroidBeautify.html(text, OPTIONS);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return text;
    }
  }
}
