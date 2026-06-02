package ir.hanzodev1375.ghostide.codeeditors.langs.html;

import io.github.rosemoe.sora.lang.completion.CompletionItemKind;
import ir.hanzodev1375.ghostide.codeeditors.lspcustomhot.CustomCompletionItem;
import java.util.ArrayList;
import java.util.List;

public final class CssHelper {

  private static final List<CssPropertyEntry> PROPERTIES = new ArrayList<>();

  static {
    initProperties();
  }

  private static void initProperties() {
    addProp("align-content", "Aligns content along the cross axis", CompletionItemKind.Property);
    addProp("align-items", "Aligns items along the cross axis", CompletionItemKind.Property);
    addProp("align-self", "Aligns an item along the cross axis", CompletionItemKind.Property);
    addProp("all", "Resets all properties", CompletionItemKind.Property);
    addProp("animation", "Animation shorthand", CompletionItemKind.Property);
    addProp("animation-delay", "Animation delay", CompletionItemKind.Property);
    addProp("animation-direction", "Animation direction", CompletionItemKind.Property);
    addProp("animation-duration", "Animation duration", CompletionItemKind.Property);
    addProp("animation-fill-mode", "Animation fill mode", CompletionItemKind.Property);
    addProp("animation-iteration-count", "Animation iteration count", CompletionItemKind.Property);
    addProp("animation-name", "Animation name", CompletionItemKind.Property);
    addProp("animation-play-state", "Animation play state", CompletionItemKind.Property);
    addProp("animation-timing-function", "Animation timing function", CompletionItemKind.Property);
    addProp("backface-visibility", "Backface visibility", CompletionItemKind.Property);
    addProp("background", "Background shorthand", CompletionItemKind.Property);
    addProp("background-attachment", "Background attachment", CompletionItemKind.Property);
    addProp("background-blur", "Background blur", CompletionItemKind.Property);
    addProp("background-clip", "Background clip", CompletionItemKind.Property);
    addProp("background-color", "Background color", CompletionItemKind.Property);
    addProp("background-image", "Background image", CompletionItemKind.Property);
    addProp("background-origin", "Background origin", CompletionItemKind.Property);
    addProp("background-position", "Background position", CompletionItemKind.Property);
    addProp("background-repeat", "Background repeat", CompletionItemKind.Property);
    addProp("background-size", "Background size", CompletionItemKind.Property);
    addProp("border", "Border shorthand", CompletionItemKind.Property);
    addProp("border-bottom", "Bottom border", CompletionItemKind.Property);
    addProp("border-bottom-color", "Bottom border color", CompletionItemKind.Property);
    addProp("border-bottom-left-radius", "Bottom left radius", CompletionItemKind.Property);
    addProp("border-bottom-right-radius", "Bottom right radius", CompletionItemKind.Property);
    addProp("border-bottom-style", "Bottom border style", CompletionItemKind.Property);
    addProp("border-bottom-width", "Bottom border width", CompletionItemKind.Property);
    addProp("border-collapse", "Border collapse", CompletionItemKind.Property);
    addProp("border-color", "Border color", CompletionItemKind.Property);
    addProp("border-image", "Border image", CompletionItemKind.Property);
    addProp("border-image-outset", "Border image outset", CompletionItemKind.Property);
    addProp("border-image-repeat", "Border image repeat", CompletionItemKind.Property);
    addProp("border-image-slice", "Border image slice", CompletionItemKind.Property);
    addProp("border-image-source", "Border image source", CompletionItemKind.Property);
    addProp("border-image-width", "Border image width", CompletionItemKind.Property);
    addProp("border-left", "Left border", CompletionItemKind.Property);
    addProp("border-left-color", "Left border color", CompletionItemKind.Property);
    addProp("border-left-style", "Left border style", CompletionItemKind.Property);
    addProp("border-left-width", "Left border width", CompletionItemKind.Property);
    addProp("border-radius", "Border radius", CompletionItemKind.Property);
    addProp("border-right", "Right border", CompletionItemKind.Property);
    addProp("border-right-color", "Right border color", CompletionItemKind.Property);
    addProp("border-right-style", "Right border style", CompletionItemKind.Property);
    addProp("border-right-width", "Right border width", CompletionItemKind.Property);
    addProp("border-spacing", "Border spacing", CompletionItemKind.Property);
    addProp("border-style", "Border style", CompletionItemKind.Property);
    addProp("border-top", "Top border", CompletionItemKind.Property);
    addProp("border-top-color", "Top border color", CompletionItemKind.Property);
    addProp("border-top-left-radius", "Top left radius", CompletionItemKind.Property);
    addProp("border-top-right-radius", "Top right radius", CompletionItemKind.Property);
    addProp("border-top-style", "Top border style", CompletionItemKind.Property);
    addProp("border-top-width", "Top border width", CompletionItemKind.Property);
    addProp("border-width", "Border width", CompletionItemKind.Property);
    addProp("bottom", "Bottom position", CompletionItemKind.Property);
    addProp("box-decoration-break", "Box decoration break", CompletionItemKind.Property);
    addProp("box-shadow", "Box shadow", CompletionItemKind.Property);
    addProp("box-sizing", "Box sizing", CompletionItemKind.Property);
    addProp("break-after", "Break after", CompletionItemKind.Property);
    addProp("break-before", "Break before", CompletionItemKind.Property);
    addProp("break-inside", "Break inside", CompletionItemKind.Property);
    addProp("caption-side", "Caption side", CompletionItemKind.Property);
    addProp("caret-color", "Caret color", CompletionItemKind.Property);
    addProp("clear", "Clear", CompletionItemKind.Property);
    addProp("clip", "Clip", CompletionItemKind.Property);
    addProp("color", "Text color", CompletionItemKind.Property);
    addProp("column-count", "Column count", CompletionItemKind.Property);
    addProp("column-fill", "Column fill", CompletionItemKind.Property);
    addProp("column-gap", "Column gap", CompletionItemKind.Property);
    addProp("column-rule", "Column rule", CompletionItemKind.Property);
    addProp("column-rule-color", "Column rule color", CompletionItemKind.Property);
    addProp("column-rule-style", "Column rule style", CompletionItemKind.Property);
    addProp("column-rule-width", "Column rule width", CompletionItemKind.Property);
    addProp("column-span", "Column span", CompletionItemKind.Property);
    addProp("column-width", "Column width", CompletionItemKind.Property);
    addProp("columns", "Columns shorthand", CompletionItemKind.Property);
    addProp("counter-increment", "Counter increment", CompletionItemKind.Property);
    addProp("counter-reset", "Counter reset", CompletionItemKind.Property);
    addProp("cursor", "Cursor", CompletionItemKind.Property);
    addProp("direction", "Direction", CompletionItemKind.Property);
    addProp("display", "Display", CompletionItemKind.Property);
    addProp("empty-cells", "Empty cells", CompletionItemKind.Property);
    addProp("filter", "Filter", CompletionItemKind.Property);
    addProp("flex", "Flex shorthand", CompletionItemKind.Property);
    addProp("flex-basis", "Flex basis", CompletionItemKind.Property);
    addProp("flex-direction", "Flex direction", CompletionItemKind.Property);
    addProp("flex-flow", "Flex flow", CompletionItemKind.Property);
    addProp("flex-grow", "Flex grow", CompletionItemKind.Property);
    addProp("flex-shrink", "Flex shrink", CompletionItemKind.Property);
    addProp("flex-wrap", "Flex wrap", CompletionItemKind.Property);
    addProp("font-family", "Font family", CompletionItemKind.Property);
    addProp("font-size", "Font size", CompletionItemKind.Property);
    addProp("font-size-adjust", "Font size adjust", CompletionItemKind.Property);
    addProp("font-stretch", "Font stretch", CompletionItemKind.Property);
    addProp("font-style", "Font style", CompletionItemKind.Property);
    addProp("font-variant", "Font variant", CompletionItemKind.Property);
    addProp("font-variant-caps", "Font variant caps", CompletionItemKind.Property);
    addProp("font-weight", "Font weight", CompletionItemKind.Property);
    addProp("gap", "Gap", CompletionItemKind.Property);
    addProp("grid", "Grid shorthand", CompletionItemKind.Property);
    addProp("grid-area", "Grid area", CompletionItemKind.Property);
    addProp("grid-auto-columns", "Grid auto columns", CompletionItemKind.Property);
    addProp("grid-auto-flow", "Grid auto flow", CompletionItemKind.Property);
    addProp("grid-auto-rows", "Grid auto rows", CompletionItemKind.Property);
    addProp("grid-column", "Grid column", CompletionItemKind.Property);
    addProp("grid-column-end", "Grid column end", CompletionItemKind.Property);
    addProp("grid-column-gap", "Grid column gap", CompletionItemKind.Property);
    addProp("grid-column-start", "Grid column start", CompletionItemKind.Property);
    addProp("grid-gap", "Grid gap", CompletionItemKind.Property);
    addProp("grid-row", "Grid row", CompletionItemKind.Property);
    addProp("grid-row-end", "Grid row end", CompletionItemKind.Property);
    addProp("grid-row-gap", "Grid row gap", CompletionItemKind.Property);
    addProp("grid-row-start", "Grid row start", CompletionItemKind.Property);
    addProp("grid-template", "Grid template", CompletionItemKind.Property);
    addProp("grid-template-areas", "Grid template areas", CompletionItemKind.Property);
    addProp("grid-template-columns", "Grid template columns", CompletionItemKind.Property);
    addProp("grid-template-rows", "Grid template rows", CompletionItemKind.Property);
    addProp("height", "Height", CompletionItemKind.Property);
    addProp("ime-mode", "IME mode", CompletionItemKind.Property);
    addProp("justify-content", "Justify content", CompletionItemKind.Property);
    addProp("left", "Left position", CompletionItemKind.Property);
    addProp("letter-spacing", "Letter spacing", CompletionItemKind.Property);
    addProp("line-break", "Line break", CompletionItemKind.Property);
    addProp("line-height", "Line height", CompletionItemKind.Property);
    addProp("list-style", "List style", CompletionItemKind.Property);
    addProp("list-style-image", "List style image", CompletionItemKind.Property);
    addProp("list-style-position", "List style position", CompletionItemKind.Property);
    addProp("list-style-type", "List style type", CompletionItemKind.Property);
    addProp("margin", "Margin", CompletionItemKind.Property);
    addProp("margin-bottom", "Margin bottom", CompletionItemKind.Property);
    addProp("margin-left", "Margin left", CompletionItemKind.Property);
    addProp("margin-right", "Margin right", CompletionItemKind.Property);
    addProp("margin-top", "Margin top", CompletionItemKind.Property);
    addProp("marker-offset", "Marker offset", CompletionItemKind.Property);
    addProp("max-height", "Max height", CompletionItemKind.Property);
    addProp("max-width", "Max width", CompletionItemKind.Property);
    addProp("min-height", "Min height", CompletionItemKind.Property);
    addProp("min-width", "Min width", CompletionItemKind.Property);
    addProp("mix-blend-mode", "Mix blend mode", CompletionItemKind.Property);
    addProp("object-fit", "Object fit", CompletionItemKind.Property);
    addProp("object-position", "Object position", CompletionItemKind.Property);
    addProp("opacity", "Opacity", CompletionItemKind.Property);
    addProp("order", "Order", CompletionItemKind.Property);
    addProp("orphans", "Orphans", CompletionItemKind.Property);
    addProp("outline", "Outline", CompletionItemKind.Property);
    addProp("outline-color", "Outline color", CompletionItemKind.Property);
    addProp("outline-offset", "Outline offset", CompletionItemKind.Property);
    addProp("outline-style", "Outline style", CompletionItemKind.Property);
    addProp("outline-width", "Outline width", CompletionItemKind.Property);
    addProp("overflow", "Overflow", CompletionItemKind.Property);
    addProp("overflow-wrap", "Overflow wrap", CompletionItemKind.Property);
    addProp("overflow-x", "Overflow X", CompletionItemKind.Property);
    addProp("overflow-y", "Overflow Y", CompletionItemKind.Property);
    addProp("padding", "Padding", CompletionItemKind.Property);
    addProp("padding-bottom", "Padding bottom", CompletionItemKind.Property);
    addProp("padding-left", "Padding left", CompletionItemKind.Property);
    addProp("padding-right", "Padding right", CompletionItemKind.Property);
    addProp("padding-top", "Padding top", CompletionItemKind.Property);
    addProp("page-break-after", "Page break after", CompletionItemKind.Property);
    addProp("page-break-before", "Page break before", CompletionItemKind.Property);
    addProp("page-break-inside", "Page break inside", CompletionItemKind.Property);
    addProp("perspective", "Perspective", CompletionItemKind.Property);
    addProp("perspective-origin", "Perspective origin", CompletionItemKind.Property);
    addProp("pointer-events", "Pointer events", CompletionItemKind.Property);
    addProp("position", "Position", CompletionItemKind.Property);
    addProp("quotes", "Quotes", CompletionItemKind.Property);
    addProp("resize", "Resize", CompletionItemKind.Property);
    addProp("right", "Right position", CompletionItemKind.Property);
    addProp("row-gap", "Row gap", CompletionItemKind.Property);
    addProp("scroll-behavior", "Scroll behavior", CompletionItemKind.Property);
    addProp("speak", "Speak", CompletionItemKind.Property);
    addProp("table-layout", "Table layout", CompletionItemKind.Property);
    addProp("tab-size", "Tab size", CompletionItemKind.Property);
    addProp("text-align", "Text align", CompletionItemKind.Property);
    addProp("text-align-last", "Text align last", CompletionItemKind.Property);
    addProp("text-decoration", "Text decoration", CompletionItemKind.Property);
    addProp("text-decoration-color", "Text decoration color", CompletionItemKind.Property);
    addProp("text-decoration-line", "Text decoration line", CompletionItemKind.Property);
    addProp("text-decoration-skip", "Text decoration skip", CompletionItemKind.Property);
    addProp("text-decoration-style", "Text decoration style", CompletionItemKind.Property);
    addProp("text-indent", "Text indent", CompletionItemKind.Property);
    addProp("text-justify", "Text justify", CompletionItemKind.Property);
    addProp("text-overflow", "Text overflow", CompletionItemKind.Property);
    addProp("text-shadow", "Text shadow", CompletionItemKind.Property);
    addProp("text-transform", "Text transform", CompletionItemKind.Property);
    addProp("text-underline-position", "Text underline position", CompletionItemKind.Property);
    addProp("top", "Top position", CompletionItemKind.Property);
    addProp("transform", "Transform", CompletionItemKind.Property);
    addProp("transform-origin", "Transform origin", CompletionItemKind.Property);
    addProp("transform-style", "Transform style", CompletionItemKind.Property);
    addProp("transition", "Transition", CompletionItemKind.Property);
    addProp("transition-delay", "Transition delay", CompletionItemKind.Property);
    addProp("transition-duration", "Transition duration", CompletionItemKind.Property);
    addProp("transition-property", "Transition property", CompletionItemKind.Property);
    addProp(
        "transition-timing-function", "Transition timing function", CompletionItemKind.Property);
    addProp("unicode-bidi", "Unicode bidi", CompletionItemKind.Property);
    addProp("vertical-align", "Vertical align", CompletionItemKind.Property);
    addProp("visibility", "Visibility", CompletionItemKind.Property);
    addProp("white-space", "White space", CompletionItemKind.Property);
    addProp("widows", "Widows", CompletionItemKind.Property);
    addProp("width", "Width", CompletionItemKind.Property);
    addProp("will-change", "Will change", CompletionItemKind.Property);
    addProp("word-break", "Word break", CompletionItemKind.Property);
    addProp("word-spacing", "Word spacing", CompletionItemKind.Property);
    addProp("word-wrap", "Word wrap", CompletionItemKind.Property);
    addProp("writing-mode", "Writing mode", CompletionItemKind.Property);
    addProp("z-index", "Z index", CompletionItemKind.Property);
  }

  private static void addProp(String name, String desc, CompletionItemKind kind) {
    PROPERTIES.add(new CssPropertyEntry(name, desc, kind));
  }

  public static List<CssCompletionItem> getAllPropertyItems() {
    List<CssCompletionItem> items = new ArrayList<>();
    for (CssPropertyEntry entry : PROPERTIES) {
      CssCompletionItem item = new CssCompletionItem(entry.name, entry.desc, "");
      item.kind(entry.kind);
      items.add(item);
    }
    return items;
  }

  public static List<CssCompletionItem> getPropertyItemsByPrefix(String prefix) {
    List<CssCompletionItem> items = new ArrayList<>();
    List<CssCompletionItem> startsWith = new ArrayList<>();
    List<CssCompletionItem> contains = new ArrayList<>();

    for (CssPropertyEntry entry : PROPERTIES) {
      if (entry.name.equals(prefix)) {
        CssCompletionItem item = new CssCompletionItem(entry.name, entry.desc, prefix);
        item.kind(entry.kind);
        items.add(0, item);
      } else if (entry.name.startsWith(prefix)) {
        CssCompletionItem item = new CssCompletionItem(entry.name, entry.desc, prefix);
        item.kind(entry.kind);
        startsWith.add(item);
      } else if (entry.name.contains(prefix)) {
        CssCompletionItem item = new CssCompletionItem(entry.name, entry.desc, prefix);
        item.kind(entry.kind);
        contains.add(item);
      }
    }

    items.addAll(startsWith);
    items.addAll(contains);
    return items;
  }

  public static List<CustomCompletionItem> getNormalProperty(String prefix) {
    List<CustomCompletionItem> list = new ArrayList<>();
    for (CssPropertyEntry entry : PROPERTIES) {
      if (prefix == null
          || prefix.isEmpty()
          || entry.name.startsWith(prefix)
          || entry.name.contains(prefix)) {
        CustomCompletionItem item =
            new CustomCompletionItem(
                entry.name, entry.desc, entry.name, entry.name.length(), prefix);
        item.kind(entry.kind);
        list.add(item);
      }
    }
    return list;
  }

  private static class CssPropertyEntry {
    String name;
    String desc;
    CompletionItemKind kind;

    CssPropertyEntry(String n, String d, CompletionItemKind k) {
      name = n;
      desc = d;
      kind = k;
    }
  }
}
