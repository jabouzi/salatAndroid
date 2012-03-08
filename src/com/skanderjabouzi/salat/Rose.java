package com.skanderjabouzi.salat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.widget.ImageView;

public class Rose extends ImageView {
  Paint paint;
  int direction = 0;

  public Rose(Context context) {
    super(context);

    paint = new Paint();
    paint.setColor(Color.WHITE);
    paint.setStrokeWidth(2);
    paint.setStyle(Style.STROKE);

    this.setImageResource(R.drawable.compass1);
  }

  @Override
  public void onDraw(Canvas canvas) {
    int height = this.getHeight();
    int width = this.getWidth();

    canvas.rotate(direction, width / 2, height / 2);
    super.onDraw(canvas);
  }

  public void setDirection(int direction) {
    this.direction = direction;
    this.invalidate();
  }

}
