package ir.hanzodev1375.ghostide.codeeditors.ui;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.github.rosemoe.sora.lang.completion.CompletionItemKind;

public class CustomCircleDrawable extends Drawable {

  private final Paint mPaint;
  private final Paint mTextPaint;
  private final CompletionItemKind mKind;
  private final boolean mCircle;

  public CustomCircleDrawable(CompletionItemKind kind, boolean circle, int color) {
    mKind = kind;
    mCircle = circle;

    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setColor(color);

    mTextPaint = new Paint();
    mTextPaint.setColor(0xFFFFFFFF);
    mTextPaint.setAntiAlias(true);
    mTextPaint.setTextSize(Resources.getSystem().getDisplayMetrics().density * 14);
    mTextPaint.setTextAlign(Paint.Align.CENTER);
  }

  public CustomCircleDrawable(CompletionItemKind kind, boolean circle) {
    this(kind, circle, getFallbackColor(kind));
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    float width = getBounds().right;
    float height = getBounds().bottom;
    float radius = width * 0.3f;

    if (mKind == CompletionItemKind.Variable || mKind == CompletionItemKind.Function) {
      canvas.drawRoundRect(4f, 4f, width - 4f, height - 4f, radius, radius, mPaint);
    } else if (mCircle) {
      canvas.drawCircle(width / 2f, height / 2f, width / 2f, mPaint);
    } else {
      canvas.drawRect(0f, 0f, width, height, mPaint);
    }

    canvas.save();
    canvas.translate(width / 2f, height / 2f);
    float textCenter = -(mTextPaint.descent() + mTextPaint.ascent()) / 2f;
    canvas.drawText(mKind.getDisplayChar(), 0f, textCenter, mTextPaint);
    canvas.restore();
  }

  @Override
  public void setAlpha(int alpha) {
    mPaint.setAlpha(alpha);
    mTextPaint.setAlpha(alpha);
  }

  @Override
  public void setColorFilter(@Nullable ColorFilter colorFilter) {
    mTextPaint.setColorFilter(colorFilter);
  }

  @Override
  public int getOpacity() {
    return PixelFormat.OPAQUE;
  }

  private static int getFallbackColor(CompletionItemKind kind) {
    switch (kind) {
      case Identifier:
        return Color.parseColor("#ffb0b0b0"); // خاکستری نقره‌ای
      case Text:
        return Color.parseColor("#ffa8a8a8"); // خاکستری متوسط
      case Method:
        return Color.parseColor("#fff4b2be"); // صورتی ملایم
      case Function:
        return Color.parseColor("#ffd4a0b0"); // صورتی ارغوانی
      case Constructor:
        return Color.parseColor("#ffc890a0"); // صورتی تیره
      case Field:
        return Color.parseColor("#fff1c883"); // زرد روشن
      case Variable:
        return Color.parseColor("#ffe0b060"); // زرد نارنجی
      case Class:
        return Color.parseColor("#ff85cce5"); // آبی آسمانی
      case Interface:
        return Color.parseColor("#ff6bb5d4"); // آبی متوسط
      case Module:
        return Color.parseColor("#ffa5d8e8"); // آبی کمرنگ
      case Property:
        return Color.parseColor("#ffcebcf4"); // بنفش ملایم
      case Unit:
        return Color.parseColor("#ffabb6bd"); // خاکستری آبی
      case Value:
        return Color.parseColor("#ffd4b87a"); // زرد کهربایی
      case Enum:
        return Color.parseColor("#ff99cb87"); // سبز چمنی
      case Keyword:
        return Color.parseColor("#ffcc7832"); // نارنجی
      case Snippet:
        return Color.parseColor("#ff98c379"); // سبز زیتونی
      case Color:
        return Color.parseColor("#ffff8a8a"); // قرمز روشن
      case Reference:
        return Color.parseColor("#ffb8a0d8"); // بنفش روشن
      case File:
        return Color.parseColor("#FFFFE957"); // زرد لیمویی
      case Folder:
        return Color.parseColor("#FFFF5781"); // صورتی سورمه‌ای
      case EnumMember:
        return Color.parseColor("#ffd4b060"); // زرد طلایی
      case Constant:
        return Color.parseColor("#ffc8a040"); // زرد طلایی تیره
      case Struct:
        return Color.parseColor("#ffc8a0d0"); // بنفش کمرنگ
      case Event:
        return Color.parseColor("#ffb08090"); // صورتی قهوه‌ای
      case Operator:
        return Color.parseColor("#ffeaabb6"); // صورتی کمرنگ
      case TypeParameter:
        return Color.parseColor("#ffc8b060"); // زرد قهوه‌ای
      case User:
        return Color.parseColor("#ff75b5d5"); // آبی تیره‌تر
      case Issue:
        return Color.parseColor("#ffff6b6b"); // قرمز تیره
      default:
        return Color.parseColor("#f70170"); // fallback
    }
  }
}
