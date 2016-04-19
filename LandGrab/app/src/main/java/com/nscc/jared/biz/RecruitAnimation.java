package com.nscc.jared.biz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.nscc.jared.landgrab.R;

public class RecruitAnimation extends View
{
    private Context context;
    private Paint paint;
    private int cellSize = 40;

    public RecruitAnimation(Context c, AttributeSet attrs)
    {
        super(c, attrs);
        this.context = c;

        paint = new Paint();
    }

    @Override
    public void onDraw(Canvas c)
    {
        c.drawColor(Color.BLACK);

        //border
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        c.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

//        paint.setTextSize(52);
//        paint.setColor(Color.WHITE);
//        c.drawText("Cell Rankings", 0, 50, paint);
//
//        int scaledSize = getResources().getDimensionPixelSize(R.dimen.usernameFontSize);
//        paint.setTextSize(scaledSize);
//        paint.setColor(Color.YELLOW);
//
//        c.drawText("jaranvil", 0, 100, paint);
//        c.drawRect(0, 105, barMaxWidth, 125, paint);
//
//        c.drawText("jaranvil", 0, 170, paint);
//        c.drawRect(0, 175, barMaxWidth, 195, paint);


    }
}
