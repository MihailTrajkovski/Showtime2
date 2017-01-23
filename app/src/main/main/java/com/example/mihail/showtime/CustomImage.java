package com.example.mihail.showtime;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * Created by mihail on 26.1.16.
 */
public class CustomImage extends ImageView
{
    public CustomImage(Context context)
    {
        super(context);
    }

    public CustomImage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomImage(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}

