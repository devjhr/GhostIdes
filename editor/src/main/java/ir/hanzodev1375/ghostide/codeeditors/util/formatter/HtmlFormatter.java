package ir.hanzodev1375.ghostide.codeeditors.util.formatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlFormatter extends BaseFormatter {

  private static final Set<String> VOID =
      new HashSet<>(
          Arrays.asList(
              "area", "base", "br", "col", "embed", "hr", "img", "input", "link", "meta", "param",
              "source", "track", "wbr"));

  private static final Set<String> INLINE =
      new HashSet<>(
          Arrays.asList(
              "a",
              "abbr",
              "acronym",
              "b",
              "bdo",
              "big",
              "br",
              "button",
              "cite",
              "code",
              "dfn",
              "em",
              "i",
              "img",
              "input",
              "kbd",
              "label",
              "map",
              "object",
              "output",
              "q",
              "s",
              "samp",
              "select",
              "small",
              "span",
              "strong",
              "sub",
              "sup",
              "textarea",
              "time",
              "tt",
              "u",
              "var"));

  private static final Set<String> PRESERVE_WS =
      new HashSet<>(Arrays.asList("pre", "script", "style", "textarea"));

  private static final Pattern MULTI_NL = Pattern.compile("\\n{3,}");
  private static final Pattern ATTR_SPLIT =
      Pattern.compile("(\\S+)\\s*=\\s*(\"[^\"]*\"|'[^']*'|\\S+)|([\\w:@.#\\-]+)");

  @Override
  public String format(String code) {
    if (code == null || code.isEmpty()) return "";

    String uuid = UUID.randomUUID().toString().replace("-", "");
    List<String> styleBlocks = new ArrayList<>();
    List<String> scriptBlocks = new ArrayList<>();
    CssFormatter css = new CssFormatter();
    JsFormatter js = new JsFormatter();

    code = replaceEmbedded(code, "style", css, styleBlocks, uuid, "STYLE");
    code = replaceEmbedded(code, "script", js, scriptBlocks, uuid, "SCRIPT");

    String result = formatHtml(code, uuid);

    result = reInject(result, styleBlocks, uuid, "STYLE");
    result = reInject(result, scriptBlocks, uuid, "SCRIPT");

    result = MULTI_NL.matcher(result).replaceAll("\n\n");
    return result.trim() + "\n";
  }

  // -------------------------------------------------------------------------
  // Embedded block handling (style / script)
  // -------------------------------------------------------------------------

  private String replaceEmbedded(
      String code, String tag, BaseFormatter fmt, List<String> store, String uuid, String key) {
    Pattern p = Pattern.compile("(?is)(<" + tag + "[^>]*>)(.*?)(</" + tag + ">)");
    Matcher m = p.matcher(code);
    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      String inner = m.group(2);
      String formatted = inner.trim().isEmpty() ? "" : fmt.format(inner);
      store.add(formatted);
      String ph = "___" + key + "_" + uuid + "_" + (store.size() - 1) + "___";
      m.appendReplacement(
          sb,
          Matcher.quoteReplacement(
              m.group(1) + (formatted.isEmpty() ? "" : "\n" + ph + "\n") + m.group(3)));
    }
    m.appendTail(sb);
    return sb.toString();
  }

  private String reInject(String result, List<String> blocks, String uuid, String key) {
    for (int i = 0; i < blocks.size(); i++) {
      String ph = "___" + key + "_" + uuid + "_" + i + "___";
      Matcher m = Pattern.compile("(?m)^([ \\t]*)" + Pattern.quote(ph) + "$").matcher(result);
      if (m.find()) {
        String pad = m.group(1);
        result = result.replace(m.group(0), indentBlock(blocks.get(i), pad));
      }
    }
    return result;
  }

  private String indentBlock(String text, String pad) {
    if (text == null || text.isEmpty()) return "";
    StringBuilder sb = new StringBuilder();
    String[] lines = text.split("\n", -1);
    for (int i = 0; i < lines.length; i++) {
      if (!lines[i].trim().isEmpty()) sb.append(pad);
      sb.append(lines[i]);
      if (i < lines.length - 1) sb.append("\n");
    }
    return sb.toString();
  }

  // -------------------------------------------------------------------------
  // Core HTML formatter
  // -------------------------------------------------------------------------

  private String formatHtml(String code, String uuid) {
    List<String> tokens = tokenise(code);
    StringBuilder out = new StringBuilder();
    int depth = 0;

    int ti = 0;
    while (ti < tokens.size()) {
      String token = tokens.get(ti);
      String trimmed = token.trim();
      if (trimmed.isEmpty()) {
        ti++;
        continue;
      }

      boolean isClose = trimmed.startsWith("</");
      boolean isComment = trimmed.startsWith("<!--");
      boolean isDoctype = trimmed.startsWith("<!");
      boolean isOpen = trimmed.startsWith("<") && !isClose && !isComment && !isDoctype;
      boolean isSelfClose = isOpen && (trimmed.endsWith("/>") || isVoid(trimmed));

      String tagName = (isOpen || isClose) ? tagName(trimmed).toLowerCase() : "";
      boolean isInline = INLINE.contains(tagName);

      // ---- DOCTYPE (no indent, no depth change) ----------------------------
      if (isDoctype) {
        out.append(trimmed).append("\n");
        ti++;
        continue;
      }

      // ---- HTML comment ----------------------------------------------------
      if (isComment) {
        out.append(getIndentString(depth)).append(trimmed).append("\n");
        ti++;
        continue;
      }

      // ---- Closing tag -----------------------------------------------------
      if (isClose) {
        // Inline closing tags: don't emit their own line; they are consumed
        // inside the inline-group branch below (or left over if unpaired —
        // emit as-is to avoid losing content).
        if (isInline) {
          // Orphaned close tag — just emit without changing depth
          out.append(trimmed);
          ti++;
          continue;
        }
        depth = Math.max(0, depth - 1);
        out.append(getIndentString(depth)).append(trimmed).append("\n");
        ti++;
        continue;
      }

      // ---- Void / self-close (always block-level) --------------------------
      if (isSelfClose) {
        out.append(getIndentString(depth))
            .append(formatTag(trimmed, getIndentString(depth)))
            .append("\n");
        ti++;
        continue;
      }

      // ---- Inline open tag: collect run on one line -----------------------
      if (isOpen && isInline) {
        // Gather everything until the matching close tag (respecting nesting)
        String pad = getIndentString(depth);
        StringBuilder inline = new StringBuilder();
        int consumed = collectInlineRun(tokens, ti, inline);
        out.append(pad).append(inline).append("\n");
        ti += consumed;
        continue;
      }

      // ---- Block open tag --------------------------------------------------
      if (isOpen) {
        String pad = getIndentString(depth);
        String formatted = formatTag(trimmed, pad);
        out.append(formatted).append("\n");
        depth++;
        ti++;
        continue;
      }

      // ---- Text node -------------------------------------------------------
      String text = trimmed;
      if (!text.isEmpty()) {
        out.append(getIndentString(depth)).append(text).append("\n");
      }
      ti++;
    }

    return out.toString();
  }

  /**
   * Collect an inline run starting at {@code start} index. An inline run is: an inline open tag +
   * all subsequent tokens (text, nested inline tags, their close tags) up to and including the
   * matching close tag.
   *
   * @param tokens full token list
   * @param start index of the first inline open tag
   * @param out StringBuilder that receives the concatenated inline string
   * @return number of tokens consumed
   */
  private int collectInlineRun(List<String> tokens, int start, StringBuilder out) {
    // We may be starting with an inline tag or a plain text node that follows
    // one — handle both.
    int i = start;
    // keep consuming as long as the next token is also inline content
    while (i < tokens.size()) {
      String token = tokens.get(i).trim();
      if (token.isEmpty()) {
        i++;
        continue;
      }

      boolean isClose = token.startsWith("</");
      boolean isOpen =
          token.startsWith("<") && !isClose && !token.startsWith("<!--") && !token.startsWith("<!");
      String tName = (isOpen || isClose) ? tagName(token).toLowerCase() : "";
      boolean isInline = INLINE.contains(tName);
      boolean isVoidTag = isOpen && isVoid(token);

      if (isOpen && isInline) {
        out.append(formatTag(token, ""));
        i++;
        // If it's a void inline (br, img, input…) don't look for close tag
        if (isVoidTag) continue;
        // Otherwise recursively collect inner content until matching </tName>
        i = collectUntilClose(tokens, i, tName, out);
        continue;
      }

      if (isClose && isInline) {
        // closing tag of an outer inline — stop here (caller handles it)
        break;
      }

      // plain text between inline tags
      if (!isOpen && !isClose) {
        out.append(token);
        i++;
        continue;
      }

      // Anything else (block tag) — stop the inline run
      break;
    }
    return i - start;
  }

  /**
   * Consume tokens from {@code start}, appending them to {@code out}, until we find the matching
   * {@code closeTag} close tag (inclusive). Handles nested same-name tags.
   *
   * @return index just past the consumed close tag
   */
  private int collectUntilClose(
      List<String> tokens, int start, String closeTag, StringBuilder out) {
    int depth = 0;
    int i = start;
    while (i < tokens.size()) {
      String token = tokens.get(i).trim();
      if (token.isEmpty()) {
        i++;
        continue;
      }

      boolean isClose = token.startsWith("</");
      boolean isOpen =
          token.startsWith("<") && !isClose && !token.startsWith("<!--") && !token.startsWith("<!");
      String tName = (isOpen || isClose) ? tagName(token).toLowerCase() : "";

      if (isOpen && tName.equals(closeTag) && !isVoid(token)) {
        depth++;
        out.append(formatTag(token, ""));
      } else if (isClose && tName.equals(closeTag)) {
        if (depth == 0) {
          out.append(token);
          i++;
          return i;
        }
        depth--;
        out.append(token);
      } else if (isOpen) {
        out.append(formatTag(token, ""));
      } else {
        out.append(token);
      }
      i++;
    }
    return i;
  }

  // -------------------------------------------------------------------------
  // Tag formatting helpers
  // -------------------------------------------------------------------------

  /** Format a single opening tag — wrap long attribute lists one-per-line. */
  private String formatTag(String tag, String baseIndent) {
    int nameEnd = 1;
    while (nameEnd < tag.length()
        && !Character.isWhitespace(tag.charAt(nameEnd))
        && tag.charAt(nameEnd) != '>'
        && tag.charAt(nameEnd) != '/') nameEnd++;
    String name = tag.substring(1, nameEnd);

    boolean selfClose = tag.endsWith("/>");
    String inner = tag.substring(nameEnd, selfClose ? tag.length() - 2 : tag.length() - 1).trim();

    if (inner.isEmpty()) {
      return baseIndent + "<" + name + (selfClose ? " />" : ">");
    }

    List<String> attrs = new ArrayList<>();
    Matcher m = ATTR_SPLIT.matcher(inner);
    while (m.find()) {
      String full = m.group(0);
      if (!full.trim().isEmpty()) attrs.add(full.trim());
    }

    if (attrs.isEmpty()) return baseIndent + "<" + name + (selfClose ? " />" : ">");

    String attrIndent = baseIndent + INDENT;
    String singleLine =
        baseIndent + "<" + name + " " + String.join(" ", attrs) + (selfClose ? " />" : ">");
    if (singleLine.length() <= 80) return singleLine;

    StringBuilder sb = new StringBuilder(baseIndent).append("<").append(name).append("\n");
    for (int i = 0; i < attrs.size(); i++) {
      sb.append(attrIndent).append(attrs.get(i));
      if (i < attrs.size() - 1) sb.append("\n");
    }
    sb.append("\n").append(baseIndent).append(selfClose ? "/>" : ">");
    return sb.toString();
  }

  // -------------------------------------------------------------------------
  // Tokeniser
  // -------------------------------------------------------------------------

  private List<String> tokenise(String html) {
    List<String> list = new ArrayList<>();
    int i = 0;
    while (i < html.length()) {
      if (html.charAt(i) == '<') {
        int end;
        if (html.startsWith("<!--", i)) {
          end = html.indexOf("-->", i + 4);
          end = end < 0 ? html.length() : end + 3;
        } else if (html.startsWith("<!", i)) {
          end = html.indexOf('>', i) + 1;
          if (end == 0) end = html.length();
        } else {
          end = i + 1;
          boolean inQ = false;
          char qc = 0;
          while (end < html.length()) {
            char c = html.charAt(end);
            if (inQ) {
              if (c == qc) inQ = false;
            } else if (c == '"' || c == '\'') {
              inQ = true;
              qc = c;
            } else if (c == '>') {
              end++;
              break;
            }
            end++;
          }
        }
        list.add(html.substring(i, end));
        i = end;
      } else {
        int end = html.indexOf('<', i);
        if (end < 0) end = html.length();
        String text = html.substring(i, end).trim();
        if (!text.isEmpty()) list.add(text);
        i = end;
      }
    }
    return list;
  }

  // -------------------------------------------------------------------------
  // Utilities
  // -------------------------------------------------------------------------

  private String tagName(String tag) {
    int s = tag.startsWith("</") ? 2 : 1;
    int e = s;
    while (e < tag.length()
        && !Character.isWhitespace(tag.charAt(e))
        && tag.charAt(e) != '>'
        && tag.charAt(e) != '/') e++;
    return tag.substring(s, e);
  }

  private boolean isVoid(String tag) {
    return VOID.contains(tagName(tag).toLowerCase());
  }
}
