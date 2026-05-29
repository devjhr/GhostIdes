package ir.theme;

import com.google.gson.annotations.SerializedName;

public class WidgetTheme {

  @SerializedName("text")
  private String text;

  @SerializedName("hint")
  private String hint;

  @SerializedName("accent")
  private String accent;

  @SerializedName("background")
  private String background;

  @SerializedName("surface")
  private String surface;

  @SerializedName("stroke")
  private String stroke;

  @SerializedName("fabBackground")
  private String fabBackground;

  @SerializedName("fabIcon")
  private String fabIcon;

  @SerializedName("tabSelected")
  private String tabSelected;

  @SerializedName("tabUnselected")
  private String tabUnselected;

  @SerializedName("imageTint")
  private String imageTint;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  public String getAccent() {
    return accent;
  }

  public void setAccent(String accent) {
    this.accent = accent;
  }

  public String getBackground() {
    return background;
  }

  public void setBackground(String background) {
    this.background = background;
  }

  public String getSurface() {
    return surface;
  }

  public void setSurface(String surface) {
    this.surface = surface;
  }

  public String getStroke() {
    return stroke;
  }

  public void setStroke(String stroke) {
    this.stroke = stroke;
  }

  public String getFabBackground() {
    return fabBackground;
  }

  public void setFabBackground(String fabBackground) {
    this.fabBackground = fabBackground;
  }

  public String getFabIcon() {
    return fabIcon;
  }

  public void setFabIcon(String fabIcon) {
    this.fabIcon = fabIcon;
  }

  public String getTabSelected() {
    return tabSelected;
  }

  public void setTabSelected(String tabSelected) {
    this.tabSelected = tabSelected;
  }

  public String getTabUnselected() {
    return tabUnselected;
  }

  public void setTabUnselected(String tabUnselected) {
    this.tabUnselected = tabUnselected;
  }

  public String getImageTint() {
    return imageTint;
  }

  public void setImageTint(String imageTint) {
    this.imageTint = imageTint;
  }
}
