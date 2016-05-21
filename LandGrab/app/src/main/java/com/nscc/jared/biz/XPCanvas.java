package com.nscc.jared.biz;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class XPCanvas extends View {
    private Context context;
    private Paint paint;
    private int orange = Color.parseColor("#ff9933");

    public double xp = 0;
    public double level = 1;

    public XPCanvas(Context c, AttributeSet attrs)
    {
        super(c, attrs);
        this.context = c;

        paint = new Paint();
    }

    @Override
    public void onDraw(Canvas c)
    {
        c.drawColor(Color.BLACK);

        double width = (xp/(level*1000)) * this.getWidth();
        int intW = (int) width;

        paint.setColor(orange);
        c.drawRect(0, 0, intW, this.getHeight(), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        float touched_x = event.getX();
        float touched_y = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN)
        {
            Toast.makeText(context, touched_x + ", " + touched_y, Toast.LENGTH_SHORT).show();
        }

        return true; // processed
    }


}
