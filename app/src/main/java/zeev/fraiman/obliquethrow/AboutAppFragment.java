package zeev.fraiman.obliquethrow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.VideoView;

import java.util.ArrayList;

public class AboutAppFragment extends Fragment {

    ListView lvAboutApp;
    ArrayList<String> aboutApp;
    ArrayList<Integer> videos;
    ArrayAdapter<String> adapter;
    VideoView vvAboutApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);

        lvAboutApp = view.findViewById(R.id.lvAboutApp);
        aboutApp = new ArrayList<>();
        videos = new ArrayList<>();
        vvAboutApp = view.findViewById(R.id.vvAboutApp);

        aboutApp.add("A simple app for learning physics");
        aboutApp.add("Input data");

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, aboutApp);
        lvAboutApp.setAdapter(adapter);

        lvAboutApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*vvAboutApp.setVideoPath("android.resource://" +
                        getContext().getPackageName() + "/" + videos.get(position));
                vvAboutApp.start();*/
            }
        });


        return view;
    }
}