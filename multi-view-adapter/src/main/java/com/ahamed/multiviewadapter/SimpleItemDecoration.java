package com.ahamed.multiviewadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * SimpleItemDecoration is a {@link RecyclerView.ItemDecoration} that can be used as a divider
 * between items of a {@link LinearLayoutManager}. It supports both {@link #HORIZONTAL} and
 * {@link #VERTICAL} orientations.
 *
 * This code has been re-used from official support library's  {@link DividerItemDecoration}
 */
public class SimpleItemDecoration implements ItemDecorator {

  public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
  public static final int VERTICAL = LinearLayout.VERTICAL;

  private static final int[] ATTRS = new int[] { android.R.attr.listDivider };
  private final Rect mBounds = new Rect();
  private Drawable mDivider;
  private int mOrientation;

  public SimpleItemDecoration(Context context, int orientation) {
    final TypedArray a = context.obtainStyledAttributes(ATTRS);
    mDivider = a.getDrawable(0);
    a.recycle();
    setOrientation(orientation);
  }

  @Override public void getItemOffsets(Rect outRect, int position, @PositionType int positionType) {
    if (positionType == POSITION_END) {
      return;
    }
    if (mOrientation == VERTICAL) {
      outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    } else {
      outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
    }
  }

  @Override public void onDraw(Canvas canvas, RecyclerView parent, View child, int position,
      @PositionType int positionType) {
    if (parent.getLayoutManager() == null) {
      return;
    }
    if (positionType == POSITION_END) {
      return;
    }
    if (mOrientation == VERTICAL) {
      drawVertical(canvas, parent, child);
    } else {
      drawHorizontal(canvas, parent, child);
    }
  }

  public void setOrientation(int orientation) {
    if (orientation != HORIZONTAL && orientation != VERTICAL) {
      throw new IllegalArgumentException(
          "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
    }
    mOrientation = orientation;
  }

  @SuppressLint("NewApi")
  private void drawVertical(Canvas canvas, RecyclerView parent, View child) {
    canvas.save();
    final int left;
    final int right;
    if (parent.getClipToPadding()) {
      left = parent.getPaddingLeft();
      right = parent.getWidth() - parent.getPaddingRight();
      canvas.clipRect(left, parent.getPaddingTop(), right,
          parent.getHeight() - parent.getPaddingBottom());
    } else {
      left = 0;
      right = parent.getWidth();
    }

    parent.getDecoratedBoundsWithMargins(child, mBounds);
    final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
    final int top = bottom - mDivider.getIntrinsicHeight();
    mDivider.setBounds(left, top, right, bottom);
    mDivider.draw(canvas);
    canvas.restore();
  }

  @SuppressLint("NewApi")
  private void drawHorizontal(Canvas canvas, RecyclerView parent, View child) {
    canvas.save();
    final int top;
    final int bottom;
    if (parent.getClipToPadding()) {
      top = parent.getPaddingTop();
      bottom = parent.getHeight() - parent.getPaddingBottom();
      canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(),
          bottom);
    } else {
      top = 0;
      bottom = parent.getHeight();
    }

    parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
    final int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
    final int left = right - mDivider.getIntrinsicWidth();
    mDivider.setBounds(left, top, right, bottom);
    mDivider.draw(canvas);
    canvas.restore();
  }
}