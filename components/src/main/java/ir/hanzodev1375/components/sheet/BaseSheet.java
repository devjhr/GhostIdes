package ir.hanzodev1375.components.sheet;

import android.content.Context;
import android.view.View;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public abstract class BaseSheet extends BottomSheetDialog {

  public BaseSheet(Context context, int styleDef) {
    super(context, styleDef);
  }

  public BaseSheet(Context context) {
    super(context);
  }

  public abstract View getView();
}
