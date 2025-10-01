package zeev.fraiman.obliquethrow;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;

public class AnimationFragment extends Fragment {

    private CircleView circleView;
    private float[] xValues;
    private float[] yValues;
    private Spinner scaleSpinner;
    float h0,v0,angle,lMax,hMax,tTotal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_animation, container, false);

        circleView = view.findViewById(R.id.circleView);
        scaleSpinner = view.findViewById(R.id.scaleSpinner);

        loadFromSharedPreferences();

        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startAnimation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void calculateTrajectory(float h0, float v0, float angle) {
        if (MainActivity.isTTS==1){
            TTSManager.getInstance().speak("Speed: " + v0 + ", Angle: " + angle + ", Height: " + h0,
                    TextToSpeech.QUEUE_FLUSH, null,null);
        }
        float g = 10f;
        float tMax = 2 * v0 * (float) Math.sin(Math.toRadians(angle)) / g;
        int numPoints = 100;
        xValues = new float[numPoints];
        yValues = new float[numPoints];

        for (int i = 0; i < numPoints; i++) {
            float t = (float) i / (numPoints - 1) * tMax;
            float x = v0 * (float) Math.cos(Math.toRadians(angle)) * t;
            float y = h0 + v0 * (float) Math.sin(Math.toRadians(angle)) * t - 0.5f * g * t * t;
            xValues[i] = x;
            yValues[i] = y;
        }
    }

    private void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(3000);

        String scaleOption = (String) scaleSpinner.getSelectedItem();
        float scaleX = getScaleFactor(xValues, circleView.getWidth());
        float scaleY = getScaleFactor(yValues, circleView.getHeight());

        float scaleFactor = scaleOption.equals("Vertical") ? scaleY : scaleX;

        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            int index = Math.min((int) (fraction * (xValues.length - 1)), xValues.length - 1);
            float x = xValues[index] * scaleFactor;
            float y = yValues[index] * scaleFactor;

            circleView.setPosition(x, circleView.getHeight() - y);
        });
        animator.start();
    }

    private float getScaleFactor(float[] values, int maxDimension) {
        float maxValue = 0;
        for (float value : values) {
            if (value > maxValue) maxValue = value;
        }
        return (maxDimension - 50) / maxValue;
    }

    private void loadFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("BasicData", getContext().MODE_PRIVATE);

        v0 = sharedPreferences.getFloat("v0", 0f);
        angle = sharedPreferences.getFloat("angle", 0f);
        h0 = sharedPreferences.getFloat("h0", 0f);
        hMax = sharedPreferences.getFloat("Hmax", 0f);
        tTotal = sharedPreferences.getFloat("Ttotal", 0f);
        lMax = sharedPreferences.getFloat("Lmax", 0f);

        calculateTrajectory(h0, v0, angle);

    }


}
