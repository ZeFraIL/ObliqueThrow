package zeev.fraiman.obliquethrow;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class PointsFragment extends Fragment {

    float h0,v0,angle,lMax,hMax,tTotal;
    String direction;
    ArrayList<Point> points;
    ArrayList<String> points_names;
    ArrayAdapter<String> adapter;
    ListView lvPoints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_points, container, false);

        lvPoints = view.findViewById(R.id.lvPoints);
        loadFromSharedPreferences();
        lvPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Point point = points.get(position);
                /*CustomAlertDialog customAlertDialog = new CustomAlertDialog(getContext());
                customAlertDialog.showPoint(point.toString());*/
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage(point.toString());
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        });
        return view;
    }

    private void loadFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("BasicData", getContext().MODE_PRIVATE);

        v0 = sharedPreferences.getFloat("v0", 0f);
        angle = sharedPreferences.getFloat("angle", 0f);
        h0 = sharedPreferences.getFloat("h0", 0f);
        hMax = sharedPreferences.getFloat("Hmax", 0f);
        tTotal = sharedPreferences.getFloat("Ttotal", 0f);
        lMax = sharedPreferences.getFloat("Lmax", 0f);
        buildPoints();
    }

    private void buildPoints() {
        points = new ArrayList<>();
        points_names = new ArrayList<>();
        int n = 50;
        float dt = tTotal / 50;
        for (int i = 0; i < n; i++) {
            float t = dt * i;
            float l = (float) (v0 * t * Math.cos(Math.toRadians(angle)));
            float h = (float) (h0 + v0 * t * Math.sin(Math.toRadians(angle)) - 0.5 * 9.81 * t * t);
            float vx = (float) (v0 * Math.cos(Math.toRadians(angle)));
            float vy = (float) (v0 * Math.sin(Math.toRadians(angle)) - 9.81 * t);
            if (vy > 0)
                direction = "Up";
            if (vy < 0)
                direction = "Down";
            if (vy == 0)
                direction = "Horizontal";
            points.add(new Point(h, l, t, vx, vy, direction));
            points_names.add("Point #" + i + "/" + n + " (" + direction + ")");
        }
        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                points_names);
        lvPoints.setAdapter(adapter);
    }
}