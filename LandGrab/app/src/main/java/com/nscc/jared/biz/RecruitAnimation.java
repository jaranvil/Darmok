package com.nscc.jared.biz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.nscc.jared.landgrab.R;

import java.util.ArrayList;

public class RecruitAnimation extends View
{
    private Context context;
    private Paint paint;
    public ArrayList<String> usernames = new ArrayList<>();
    public ArrayList<Integer> supporters = new ArrayList<>();
    public boolean done = false;

    private int[] colors = {Color.parseColor("#0000cc"), Color.parseColor("#009933"), Color.parseColor("#990000"), Color.parseColor("#9900cc"), Color.parseColor("#996633"), Color.parseColor("#009933"), Color.parseColor("#0000cc")};
    private int counter = 0;
    private int speed = 60;

    public RecruitAnimation(Context c, AttributeSet attrs)
    {
        super(c, attrs);
        this.context = c;

        paint = new Paint();
    }

    @Override
    public void onDraw(Canvas c)
    {
        counter += speed;
        c.drawColor(Color.BLACK);

        int canvasHeight = this.getHeight();

        if (usernames != null)
        {
            // find sum of support
            double total = 0;
            for (int i = 0; i < supporters.size();i++)
            {
                total += supporters.get(i);
            }

            // draw each support
            int lastBottom = 0;
            for (int i = 0;i < usernames.size();i++)
            {
                if (i < colors.length-1)
                {
                    double supportAmount = supporters.get(i);
                    double _height = (supportAmount/total) * canvasHeight;
                    int height = (int) _height;
                    int amount = (int) supportAmount;
                    int intTotal = (int) total;
                    float percent = amount * 100f / intTotal;

                    //adjust height for drawing
                    if (counter < height)
                        height = counter;

                    paint.setColor(colors[i]);
                    c.drawRect(this.getWidth()-100, lastBottom, this.getWidth(), lastBottom + height, paint);

                    if (height > 50)
                    {
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(45);
                        c.drawText(usernames.get(i) + " ("+Math.round(percent)+"%)", 10, lastBottom + (height / 2), paint);
                    }

                    lastBottom += height;
                }
                else
                    break;

            }
        }








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
