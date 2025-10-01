package zeev.fraiman.obliquethrow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {

    private float h0, v0, angle;
    private float maxL, maxH, margin;
    private final float g = 10f;
    private Paint paint;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFF0000FF);
        paint.setStrokeWidth(5f);
    }

    public void setInitialValues(float h0, float v0, float angle, float maxL, float maxH, float margin) {
        this.h0 = h0;
        this.v0 = v0;
        this.angle = angle;
        this.maxL = maxL;
        this.maxH = maxH;
        this.margin = margin;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float t = 0;
        float dt = 0.1f;
        float prevX = 0, prevY = h0;


        float scaleX = (getWidth() - 2 * margin) / maxL;
        float scaleY = (getHeight() - 2 * margin) / maxH;
        float scale = Math.min(scaleX, scaleY);

        while (true) {
            t += dt;
            float h = h0 + v0 * (float) Math.sin(Math.toRadians(angle)) * t - 0.5f * g * t * t;
            float l = v0 * (float) Math.cos(Math.toRadians(angle)) * t;

            if (h <= 0) break;

            float x = l * scale + margin;
            float y = h * scale + margin;

            canvas.drawLine(prevX, getHeight() - prevY, x, getHeight() - y, paint);

            prevX = x;
            prevY = y;
        }
    }
}
