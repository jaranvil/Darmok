package com.nscc.jared.biz;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FlagCanvas extends View {
    private Context context;
    private Paint paint;
    private int cellSize = 40;

    public FlagCanvas(Context c, AttributeSet attrs)
    {
        super(c, attrs);
        this.context = c;

        paint = new Paint();
    }

    @Override
    public void onDraw(Canvas c)
    {
        c.drawColor(Color.WHITE);

        int width = c.getWidth();
        int height = c.getHeight();
        int horizontalLine = height/cellSize;
        int verticalLine = width/cellSize;

        paint.setColor(Color.GRAY);
        for (int i = 0;i < horizontalLine;i++)
        {
            c.drawLine(0, i*cellSize, width, i*cellSize, paint);
        }
        for (int i = 0;i < verticalLine;i++)
        {
            c.drawLine(i*cellSize, 0, i*cellSize, height, paint);
        }
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
