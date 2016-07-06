package com.softcloud.clickableimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Softcloud on 16/7/6.
 */
public class YUVImageButton extends ImageButton {

    private static final float[] SRC_ARRAY_PRE_CONCAT_RGB_TO_YUV = {
            0.299f, 0.587f, 0.114f, 0, 0
            , -0.14713f, -0.28886f, 0.436f, 0, 0
            , 0.615f, -0.51499f, -0.10001f, 0, 0
            , 0, 0, 0, 1, 0
    };
    private static final float[] SRC_ARRAY_PRE_CONCAT_YUV_TO_RGB = {
            1, 0, 1.13983f, 0, 0
            , 1, -0.39465f, -0.58060f, 0, 0
            , 1, 2.03211f, 0, 0, 0
            , 0, 0, 0, 1, 0
    };
    private static final ColorMatrix MATRIX_PRE_CONCAT_RGB_TO_YUV = new ColorMatrix(
            SRC_ARRAY_PRE_CONCAT_RGB_TO_YUV);
    private static final ColorMatrix MATRIX_PRE_CONCAT_YUV_TO_RGB = new ColorMatrix(
            SRC_ARRAY_PRE_CONCAT_YUV_TO_RGB);

    private ColorMatrix colorMatrix;
    private float yScale;
    private float uScale;
    private float vScale;

    public YUVImageButton(Context context) {
        this(context, null);
    }

    public YUVImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YUVImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resetColorMatrix();
        yScale = 1f;
        uScale = 10f;
        vScale = 10f;
    }

    public void setupYUVScale(float yScale, float uScale, float vScale) {
        this.yScale = yScale;
        this.uScale = uScale;
        this.vScale = vScale;
    }

    private void resetColorMatrix() {
        if (colorMatrix == null) {
            colorMatrix = new ColorMatrix();
        }
        colorMatrix.reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(drawableToBitmap(getDrawable()), 0, 0, paint);
    }

    private void refreshImageWithYUVScale() {
        resetColorMatrix();
        colorMatrix.preConcat(MATRIX_PRE_CONCAT_RGB_TO_YUV);
        float[] a = colorMatrix.getArray();
        a[0] *= yScale;
        a[6] *= uScale;
        a[12] *= vScale;
        colorMatrix.postConcat(MATRIX_PRE_CONCAT_YUV_TO_RGB);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                refreshImageWithYUVScale();
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                colorMatrix.reset();
                invalidate();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

    }
}
