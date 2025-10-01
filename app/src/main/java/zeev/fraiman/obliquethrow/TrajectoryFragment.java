package zeev.fraiman.obliquethrow;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrajectoryFragment extends Fragment {

    private CustomView customView;
    private float margin = 50f;
    float h0, v0, angle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trajectory, container, false);

        customView = view.findViewById(R.id.customView);
        loadFromSharedPreferences();

        return view;
    }

    private void loadFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("BasicData", getContext().MODE_PRIVATE);

        v0 = sharedPreferences.getFloat("v0", 0f);
        angle = sharedPreferences.getFloat("angle", 0f);
        h0 = sharedPreferences.getFloat("h0", 0f);

        float maxT = 2 * v0 * (float) Math.sin(Math.toRadians(angle)) / 10;
        float maxL = v0 * (float) Math.cos(Math.toRadians(angle)) * maxT;
        float maxH = h0 + (v0 * v0 * (float) Math.sin(Math.toRadians(angle)) * (float) Math.sin(Math.toRadians(angle))) / (2 * 10); // Maximum height

        customView.setInitialValues(h0, v0, angle, maxL, maxH, margin);
        customView.invalidate();
    }
}