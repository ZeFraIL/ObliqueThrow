package zeev.fraiman.obliquethrow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ConceptsFragment extends Fragment {

    Spinner spConcept;
    String[] concept = {"Speed","Angle","Initial Speed","Height","Initial Height"," Range"};
    ArrayAdapter<String> adapter;
    TextView tvConcept;
    private String[] concepts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_concepts, container, false);

        spConcept = view.findViewById(R.id.spConcepts);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, concept);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spConcept.setAdapter(adapter);
        tvConcept = view.findViewById(R.id.tvConcept);
        concepts = getResources().getStringArray(R.array.consepts);

        spConcept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvConcept.setText(concept[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
}