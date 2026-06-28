package ir.hanzodev1375.ghostide.codeeditors.preview;

public class Match {
  final String path;
  final int startColumn;
  final int endColumn;

  public Match(String path, int startColumn, int endColumn) {
    this.path = path;
    this.startColumn = startColumn;
    this.endColumn = endColumn;
  }
}
