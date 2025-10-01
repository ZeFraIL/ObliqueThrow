package zeev.fraiman.obliquethrow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

public class CustomToast {

    public static void showCustomToast(Context context, String message,
                                       int imageResId, String backgroundColor,
                                       int fontResId, String textColor) {
        Typeface font = ResourcesCompat.getFont(context, fontResId);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(16, 16, 16, 16);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageResId);
        imageView.setPadding(0, 0, 8, 0);

        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setTextColor(Color.parseColor(textColor));
        textView.setTypeface(font);
        textView.setTextSize(16);

        layout.addView(imageView);
        layout.addView(textView);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor(backgroundColor));
        background.setCornerRadius(25);
        layout.setBackground(background);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        toast.show();
    }
}