package com.android.destranger.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ximing on 2015/5/16.
 */
public class MyEditText extends EditText {
    private String hint = "";

    public void setHint(String hint) {
        this.hint = hint;
    }

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setColor(Color.BLACK);
        canvas.drawText(this.hint,10, getHeight() / 2 + 5, paint);
        super.onDraw(canvas);
    }
}
