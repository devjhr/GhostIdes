package ir.hanzodev1375.ghostide.customui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import ir.hanzodev1375.ghostide.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.color.MaterialColors;
import ir.hanzodev1375.ghostide.codeeditors.setting.PreferencesUtils;

public class MaterialGradientCard extends FrameLayout {

  private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint lightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint radialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private PreferencesUtils setting;

  private final RectF rect = new RectF();

  private float radius;

  public MaterialGradientCard(@NonNull Context context) {
    super(context);
    init();
  }

  public MaterialGradientCard(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MaterialGradientCard(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {

    setWillNotDraw(false);

    radius = dp(28);

    setPadding(dp(20), dp(20), dp(20), dp(20));

    strokePaint.setStyle(Paint.Style.STROKE);
    strokePaint.setStrokeWidth(dp(1));

    bgPaint.setDither(true);
    lightPaint.setDither(true);
    radialPaint.setDither(true);
    setting = new PreferencesUtils(getContext());
  }

  @Override
  protected void onDraw(Canvas canvas) {

    super.onDraw(canvas);

    rect.set(0, 0, getWidth(), getHeight());

    int surface = MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSurface);

    int high =
        MaterialColors.getColor(this, com.google.android.material.R.attr.colorSurfaceContainerHigh);

    int highest =
        MaterialColors.getColor(
            this, com.google.android.material.R.attr.colorSurfaceContainerHighest);

    int outline =
        MaterialColors.getColor(this, com.google.android.material.R.attr.colorOutlineVariant);
    bgPaint.setShader(
        new LinearGradient(
            0,
            0,
            getWidth(),
            getHeight(),
            new int[] {
              setting.isShowBackground()
                  ? get(R.attr.colorPrimaryContainer, 0.30f)
                  : get(R.attr.colorPrimaryContainer, 0.12f),
              setting.isShowBackground()
                  ? get(R.attr.colorOnPrimary, 0.12f)
                  : get(R.attr.colorOnPrimary, 0.8f),
              setting.isShowBackground()
                  ? get(R.attr.colorOnPrimaryFixed, 0.18f)
                  : get(R.attr.colorOnPrimaryFixed)
            },
            new float[] {.25f, .65f, 1f},
            Shader.TileMode.CLAMP));

    canvas.drawRoundRect(rect, radius, radius, bgPaint);

    lightPaint.setShader(
        new LinearGradient(
            -getWidth() / 3f,
            0,
            getWidth(),
            getHeight(),
            new int[] {
              Color.argb(35, 255, 255, 255),
              Color.argb(12, 255, 255, 255),
              Color.argb(0, 255, 255, 255)
            },
            new float[] {0f, .35f, 1f},
            Shader.TileMode.CLAMP));

    canvas.drawRoundRect(rect, radius, radius, lightPaint);

    // نور نرم گوشه
    radialPaint.setShader(
        new RadialGradient(
            getWidth() * 0.25f,
            getHeight() * 0.15f,
            getWidth() * 0.8f,
            new int[] {Color.argb(20, 255, 255, 255), Color.TRANSPARENT},
            new float[] {0f, 1f},
            Shader.TileMode.CLAMP));

    canvas.drawRoundRect(rect, radius, radius, radialPaint);
    strokePaint.setShader(
        new LinearGradient(
            0,
            0,
            getWidth(),
            getHeight(),
            new int[] {
              Color.argb(70, Color.red(outline), Color.green(outline), Color.blue(outline)),
              Color.argb(20, Color.red(outline), Color.green(outline), Color.blue(outline))
            },
            null,
            Shader.TileMode.CLAMP));

    canvas.drawRoundRect(rect, radius, radius, strokePaint);
  }

  int get(int id) {
    return MaterialColors.getColor(this, id);
  }

  int get(int id, float alpha) {
    return MaterialColors.compositeARGBWithAlpha(
        MaterialColors.getColor(this, id), Math.round(alpha * 255));
  }

  private int dp(int dp) {

    return (int)
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
  }
}
