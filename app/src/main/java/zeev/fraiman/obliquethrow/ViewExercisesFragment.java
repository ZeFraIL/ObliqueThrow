package zeev.fraiman.obliquethrow;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewExercisesFragment extends Fragment {

    ListView listViewExercises;
    ArrayList<Exercise> exercises;
    ArrayList<String> exercises_numbers;
    ArrayAdapter<String> adapter;
    HelperDB helperDB;
    SQLiteDatabase db;

    public ViewExercisesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_exercises, container, false);
        
        listViewExercises = view.findViewById(R.id.listViewExercises);
        exercises = new ArrayList<>();
        exercises_numbers = new ArrayList<>();
        helperDB = new HelperDB(getContext());
        db = helperDB.getReadableDatabase();
        getAllExercises();

        listViewExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise exercise = exercises.get(position);
                String info="V0: "+exercise.getV0()+"\nAngle: "+exercise.getAngle()+"\nH0: "+exercise.getH0()+"\nHMax: "+exercise.getHMax()+"\nLMax: "+exercise.getLMax()+"\nTTotal: "+exercise.getTTotal();
                AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
                adb.setTitle("Exercise #"+exercises_numbers.get(position));
                adb.setMessage(info);
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = helperDB.getWritableDatabase();
                        db.delete(helperDB.EXERCISES_TABLE,
                                helperDB.EXERCISE_V0+"=? AND "+helperDB.EXERCISE_ANGLE+"=? AND "
                                        +helperDB.EXERCISE_HEIGHT+"=? AND "+helperDB.EXERCISE_HEIGHT_MAX+"=? AND "
                                        +helperDB.EXERCISE_LENGTH_MAX+"=? AND "+helperDB.EXERCISE_TOTAL_TIME+"=?",
                                new String[]{exercise.getV0(), exercise.getAngle(), exercise.getH0(), exercise.getHMax(),
                                                exercise.getLMax(), exercise.getTTotal()});
                        db.close();
                        getAllExercises();
                    }
                });
                adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adb.setNegativeButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = requireContext().
                                getSharedPreferences("DataForSend", getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putFloat("v0", Float.parseFloat(exercise.getV0()));
                        editor.putFloat("angle", Float.parseFloat(exercise.getAngle()));
                        editor.putFloat("h0", Float.parseFloat(exercise.getAngle()));
                        editor.putFloat("Hmax", Float.parseFloat(exercise.getHMax()));
                        editor.putFloat("Ttotal", Float.parseFloat(exercise.getTTotal()));
                        editor.putFloat("Lmax", Float.parseFloat(exercise.getLMax()));
                        editor.commit();
                        SendDataFragment sendDataFragment = new SendDataFragment();
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, sendDataFragment);
                        transaction.commit();
                    }
                });
                adb.show();
            }
        });

        return view;
    }

    /*private void adbSend(Exercise exercise) {
        AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
        adb.setTitle("Send exercise?");
        adb.setPositiveButton("Send by SMS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.setNegativeButton("Send by Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }*/

    public void getAllExercises() {
        ProgressBar progressBar = new ProgressBar(getContext());
        TextView progressText = new TextView(getContext());
        progressText.setText("Reading data, please wait");
        progressText.setGravity(Gravity.CENTER);
        progressText.setTextSize(18);
        progressText.setPadding(20, 20, 20, 20);

        FrameLayout layout = new FrameLayout(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        );
        layout.addView(progressBar, params);
        layout.addView(progressText, params);

        AlertDialog progressDialog = new AlertDialog.Builder(getContext())
                .setView(layout)
                .setCancelable(false)
                .create();
        progressDialog.show();

        new Thread(() -> {
            ArrayList<Exercise> exercisesTemp = new ArrayList<>();
            ArrayList<String> exercisesNumbersTemp = new ArrayList<>();
            SQLiteDatabase db = null;
            Cursor cursor = null;
            boolean isEmpty = false;

            try {
                db = helperDB.getReadableDatabase();
                cursor = db.query(helperDB.EXERCISES_TABLE, null, null, null, null, null, null);

                if (cursor.getCount() == 0) {
                    isEmpty = true;
                } else {
                    int number = 1;
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String stV0 = cursor.getString((int)cursor.getColumnIndex(helperDB.EXERCISE_V0));
                        String stAngle = cursor.getString((int)cursor.getColumnIndex(helperDB.EXERCISE_ANGLE));
                        String stH0 = cursor.getString((int)cursor.getColumnIndex(helperDB.EXERCISE_HEIGHT));
                        String stHMax = cursor.getString((int)cursor.getColumnIndex(helperDB.EXERCISE_HEIGHT_MAX));
                        String stLMax = cursor.getString((int)cursor.getColumnIndex(helperDB.EXERCISE_LENGTH_MAX));
                        String stTTotal = cursor.getString((int)cursor.getColumnIndex(helperDB.EXERCISE_TOTAL_TIME));

                        Exercise exercise = new Exercise(stV0, stAngle, stH0, stHMax, stLMax, stTTotal);
                        exercisesTemp.add(exercise);
                        exercisesNumbersTemp.add("Exercise #" + number);
                        number++;
                        cursor.moveToNext();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }

            boolean finalIsEmpty = isEmpty;
            new Handler(Looper.getMainLooper()).post(() -> {
                progressDialog.dismiss();

                if (finalIsEmpty) {
                    CustomToast.showCustomToast(getContext(), "Database of Exercises is empty",
                            R.drawable.problem, "#FF0000", R.font.pacifico, "#FFFFFF");
                } else {
                    exercises.clear();
                    exercises.addAll(exercisesTemp);

                    exercises_numbers.clear();
                    exercises_numbers.addAll(exercisesNumbersTemp);

                    adapter = new ArrayAdapter<>(
                            getContext(),
                            android.R.layout.simple_list_item_1,
                            exercises_numbers
                    );
                    listViewExercises.setAdapter(adapter);
                }
            });
        }).start();
    }


    /*public void getAllExercises() {
        int number=1;
        db = helperDB.getReadableDatabase();
        Cursor cursor = db.query(helperDB.EXERCISES_TABLE,
                null, null, null,
                null, null, null);
        if (cursor.getCount() == 0) {
            db.close();
            My_Toast.showToast(getContext(), "Database of Exercises is empty", 11);
            return;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String stV0 = cursor.getString((int) cursor.getColumnIndex(helperDB.EXERCISE_V0));
            String stAngle = cursor.getString((int) cursor.getColumnIndex(helperDB.EXERCISE_ANGLE));
            String stH0 = cursor.getString((int) cursor.getColumnIndex(helperDB.EXERCISE_HEIGHT));
            String stHMax = cursor.getString((int) cursor.getColumnIndex(helperDB.EXERCISE_HEIGHT_MAX));
            String stLMax = cursor.getString((int) cursor.getColumnIndex(helperDB.EXERCISE_LENGTH_MAX));
            String stTTotal = cursor.getString((int) cursor.getColumnIndex(helperDB.EXERCISE_TOTAL_TIME));
            Exercise exercise = new Exercise(stV0, stAngle, stH0, stHMax, stLMax, stTTotal);
            exercises.add(exercise);
            exercises_numbers.add("Exercise #" + number);
            number++;
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                exercises_numbers);
        listViewExercises.setAdapter(adapter);
    }*/
}