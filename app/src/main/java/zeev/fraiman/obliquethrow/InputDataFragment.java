package zeev.fraiman.obliquethrow;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class InputDataFragment extends Fragment {

    private EditText editTextH0, editTextV0, editTextAngle;
    String H0, V0, Angle;
    float h0=0,v0=0,angle=0;
    Button bCalcResults;
    CheckBox chbHmax, chbLmax, chbTtotal;
    double hMax=0, lMax=0, tTotal=0;
    HelperDB helperDB;
    SQLiteDatabase db;
    private String message="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input_data, container, false);

        editTextH0 = view.findViewById(R.id.editTextH0);
        editTextV0 = view.findViewById(R.id.editTextV0);
        editTextAngle = view.findViewById(R.id.editTextAngle);
        bCalcResults=view.findViewById(R.id.bCalcResults);
        chbHmax=view.findViewById(R.id.chbHmax);
        chbLmax=view.findViewById(R.id.chbLmax);
        chbTtotal=view.findViewById(R.id.chbTtotal);

        chbTtotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chbTtotal.isChecked())
                    chbTtotal.setText("T(total)="+tTotal);
                else
                    chbTtotal.setText("T(total)");
            }
        });

        chbHmax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chbHmax.isChecked())
                    chbHmax.setText("H(max)="+hMax);
                else
                    chbHmax.setText("H(max)");
            }
        });

        chbLmax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chbLmax.isChecked())
                    chbLmax.setText("L(max)="+lMax);
                else
                    chbLmax.setText("L(max)");
            }
        });
        bCalcResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                H0=editTextH0.getText().toString();
                h0 = Float.parseFloat(H0);
                V0=editTextV0.getText().toString();
                v0 = Float.parseFloat(V0);
                Angle=editTextAngle.getText().toString();
                angle = Float.parseFloat(Angle);
                //IF!!!
                double angleRadians = Math.toRadians(angle);
                final double g = 9.8;
                hMax = h0 + (Math.pow(v0 * Math.sin(angleRadians), 2) / (2 * g));
                tTotal = (v0 * Math.sin(angleRadians) +
                        Math.sqrt(Math.pow(v0 * Math.sin(angleRadians), 2) + 2 * g * h0)) / g;
                lMax = (v0 * Math.cos(angleRadians) / g) *
                        (v0 * Math.sin(angleRadians) +
                                Math.sqrt(Math.pow(v0 * Math.sin(angleRadians), 2) + 2 * g * h0));

                saveToSharedPreferences(v0, angle, h0, hMax, lMax, tTotal);
            }
        });

        return view;
    }

    private void saveToSharedPreferences(float velocity, float angle, float height, double hMax, double lMax, double tTotal) {
        AlertDialog.Builder adb=new AlertDialog.Builder(requireContext());
        adb.setTitle("Used data?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("BasicData", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("v0", velocity);
                editor.putFloat("angle", angle);
                editor.putFloat("h0", height);
                editor.putFloat("Hmax", (float) hMax);
                editor.putFloat("Ttotal", (float) tTotal);
                editor.putFloat("Lmax", (float) lMax);
                editor.apply(); // Or editor.commit();
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.setNeutralButton("Used and saved", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //saveExer();
                //saveExerNow();
                saveExerWithThread();
            }
        });
        adb.create().show();
    }

    /*private void saveExerNow() {
        helperDB=new HelperDB(getContext());
        db=helperDB.getWritableDatabase();
        db = helperDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helperDB.EXERCISE_V0, V0);
        contentValues.put(helperDB.EXERCISE_ANGLE, Angle);
        contentValues.put(helperDB.EXERCISE_HEIGHT, H0);
        contentValues.put(helperDB.EXERCISE_HEIGHT_MAX, ""+hMax);
        contentValues.put(helperDB.EXERCISE_LENGTH_MAX, ""+ lMax);
        contentValues.put(helperDB.EXERCISE_TOTAL_TIME, ""+ tTotal);
        db.insert(helperDB.EXERCISES_TABLE, null, contentValues);
        db.close();
    }*/

    private void saveExerWithThread() {
        new Thread(() -> {
            message="Message";
            SQLiteDatabase db = null;

            try {
                helperDB = new HelperDB(getContext());
                db = helperDB.getWritableDatabase();
                //saveToSharedPreferences(v0, angle, h0, hMax, lMax, tTotal);
                ContentValues contentValues = new ContentValues();
                contentValues.put(helperDB.EXERCISE_V0, ""+v0);
                contentValues.put(helperDB.EXERCISE_ANGLE, ""+angle);
                contentValues.put(helperDB.EXERCISE_HEIGHT, ""+h0);
                contentValues.put(helperDB.EXERCISE_HEIGHT_MAX, ""+hMax);
                contentValues.put(helperDB.EXERCISE_LENGTH_MAX, ""+lMax);
                contentValues.put(helperDB.EXERCISE_TOTAL_TIME, ""+tTotal);
                long rowId = db.insert(helperDB.EXERCISES_TABLE, null, contentValues);
                if (rowId != -1) {
                    message = "The entry has been added successfully!";
                } else {
                    message = "Error adding record";
                }
            } catch (Exception e) {
                message = "Database error: " + e.getMessage();
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.close();
                }
            }

            String finalMessage = message;
            new Handler(Looper.getMainLooper()).post(() ->
                    CustomToast.showCustomToast(getContext(),
                            "M="+finalMessage,
                            R.drawable.success, "#4CAF50", R.font.pacifico, "#FFFFFF")

            );
        }).start();
    }

    private void saveExer() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                helperDB=new HelperDB(getContext());
                db=helperDB.getWritableDatabase();
                db = helperDB.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(helperDB.EXERCISE_V0, V0);
                contentValues.put(helperDB.EXERCISE_ANGLE, Angle);
                contentValues.put(helperDB.EXERCISE_HEIGHT, H0);
                contentValues.put(helperDB.EXERCISE_HEIGHT_MAX, ""+hMax);
                contentValues.put(helperDB.EXERCISE_LENGTH_MAX, ""+ lMax);
                contentValues.put(helperDB.EXERCISE_TOTAL_TIME, ""+ tTotal);
                db.insert(helperDB.EXERCISES_TABLE, null, contentValues);
                db.close();

                if (getActivity() instanceof Activity) {
                    (getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomToast.showCustomToast(getContext(),
                                    "Saved Ok",
                                    R.drawable.success, "#4CAF50", R.font.pacifico, "#FFFFFF");
                        }
                    });
                }
            }
        });
    }
}