package com.codingpixel.healingbudz.customeUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;

public class ImageShadow extends TextView {
    protected Drawable bg;
    protected Paint paint;
    protected Rect padding = new Rect();
    protected Bitmap bmp;
    public ImageShadow(Context context) {
        super(context);
        init();
    }

    public ImageShadow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageShadow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressLint("NewApi")
    protected void init() {
        // decode the 9patch drawable
        bg = getResources().getDrawable(R.drawable.ic_heart);

        // get paddings from the 9patch and apply them to the View
        bg.getPadding(padding);
        setPadding(padding.left, padding.top, padding.right, padding.bottom);
        // prepare the Paint to use below
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(0,0,0));
        paint.setStyle(Paint.Style.FILL);

        // this check is needed in order to get this code
        // working if target SDK>=11
        if( Build.VERSION.SDK_INT >= 11 )
            setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        // set the shadowLayer
        paint.setShadowLayer(
                padding.left * .2f, // radius
                0f, // blurX
                padding.left * .1f, // blurY
                Color.argb(0, 0, 0, 0) // shadow color
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        // set 9patch bounds according to view measurement
        // NOTE: if not set, the drawable will not be drawn
        bg.setBounds(0, 0, w, h);

        // this code looks expensive: let's do once
        if( bmp == null ) {
            // it seems like shadowLayer doesn't take into account
            // alpha channel in ARGB_8888 sources...
            bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            // draw the given 9patch on the brand new bitmap
            Canvas tmp = new Canvas(bmp);
            bg.draw(tmp);
            // extract only the alpha channel
            bmp = bmp.extractAlpha();
        }

        // this "alpha mask" has the same shape of the starting 9patch,
        // but filled in white and **with the dropshadow**!!
        canvas.drawBitmap(bmp, 0, 0, paint);

        // let's paint the 9patch over...
        bg.draw(canvas);

        super.onDraw(canvas);
    }
}