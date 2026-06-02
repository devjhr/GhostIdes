package ir.hanzodev1375.ghostide.models;

public class ToolbarModel {
  private final int icon;
  private final String tag;

  public ToolbarModel(int icon, String tag) {
    this.icon = icon;
    this.tag = tag;
  }

  public int getIcon() {
    return this.icon;
  }

  public String getTag() {
    return this.tag;
  }
}
