package zeev.fraiman.obliquethrow;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FormulasFragment extends Fragment {

    int n=10;
    private LinearLayout LL_formulas;
    private final ImageButton[] ImageButtons = new ImageButton[n];
    private final TextView[] textViews = new TextView[n];
    private final int[] textFiles={R.raw.f1,R.raw.f2,R.raw.f3,R.raw.f4,R.raw.f5,R.raw.f6,R.raw.f7,R.raw.f8,R.raw.f9,R.raw.f1};
    int indexTV=-1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formulas, container, false);

        LL_formulas = view.findViewById(R.id.LL_formulas);

        for (int i = 0; i < n; i++) {
            int widthInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    300, getResources().getDisplayMetrics());

            ImageButton ImageButton = new ImageButton(getContext());
            String resourceName = "f" + (i + 1);
            int resourceId = getResources().getIdentifier(resourceName, "drawable", getContext().getPackageName());
            ImageButton.setImageResource(resourceId);
            LinearLayout.LayoutParams ImageButtonParams = new LinearLayout.LayoutParams(
                    widthInPx,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ImageButtonParams.bottomMargin = 30;
            ImageButton.setLayoutParams(ImageButtonParams);

            TextView textView = new TextView(getContext());
            textView.setText("Part #" + (i + 1));
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setVisibility(View.GONE);

            final int index = i;
            ImageButton.setOnClickListener(v -> {
                if (textViews[index].getVisibility() == View.GONE) {
                    indexTV=index;
                    new ReadFileTask().execute();
                    textViews[index].setVisibility(View.VISIBLE);
                } else {
                    textViews[index].setVisibility(View.GONE);
                }
            });

            LL_formulas.addView(ImageButton);
            LL_formulas.addView(textView);

            ImageButtons[i] = ImageButton;
            textViews[i] = textView;
        }

        return view;
    }

    private class ReadFileTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(requireContext(), "Please wait, reading text from file",
                    Toast.LENGTH_SHORT).show();
            CustomToast.showCustomToast(requireContext(), "Please wait, reading text from file",
                    R.drawable.physics_logo, "#FFFFFF", R.font.yatra_one, "#000000");
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                InputStream inputStream = getResources().openRawResource(textFiles[indexTV]);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textViews[indexTV].setText(result);
            CustomToast.showCustomToast(requireContext(),
                    "Reading completed",
                    R.drawable.success, "#4CAF50", R.font.pacifico, "#FFFFFF");
            if (MainActivity.isTTS==1)
                TTSManager.getInstance().speak("Reading completed",
                        TextToSpeech.QUEUE_FLUSH, null,null);
        }
    }

}