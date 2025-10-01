package zeev.fraiman.obliquethrow;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupFragment extends Fragment {

    EditText etUsernameR, etPasswordR, etEmailR;
    String stName="", stPassword="", stEmail="";
    Button bSignup;
    HelperDB helperDB;
    SQLiteDatabase db;
    MediaPlayer mp;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_signup, container, false);

        user=new User("","","");
        etUsernameR=view.findViewById(R.id.etUsernameR);
        etPasswordR=view.findViewById(R.id.etPasswordR);
        etEmailR=view.findViewById(R.id.etEmailR);
        bSignup=view.findViewById(R.id.bSignup);
        helperDB=new HelperDB(getContext());
        mp=MediaPlayer.create(getContext(),R.raw.alarm);

        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stName=etUsernameR.getText().toString();
                if (stName.equals("")) {
                    CustomToast.showCustomToast(getContext(),
                            "Write a name -- a must!",
                            R.drawable.problem, "#FF0000",
                            R.font.pacifico, "#FFFFFF");
                    TTSManager.getInstance().speak("Write a name -- a must!",
                            TextToSpeech.QUEUE_FLUSH,null,null);
                    mp.start();
                    return;
                }
                etPasswordR.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        etPasswordR.setBackgroundColor(Color.WHITE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                stPassword=etPasswordR.getText().toString();
                if (stPassword.equals("")) {
                    CustomToast.showCustomToast(getContext(),
                            "Write a password -- a must!",
                            R.drawable.problem, "#FF0000",
                            R.font.pacifico, "#FFFFFF");
                    if (WelcomeUser.isTTS==1)
                        TTSManager.getInstance().speak("Write a password -- a must!",
                            TextToSpeech.QUEUE_FLUSH,null,null);
                    mp.start();
                    return;
                }
                if (stPassword.length()<6) {
                    CustomToast.showCustomToast(getContext(),
                            "Minimum 6 characters!",
                            R.drawable.problem, "#FF0000",
                            R.font.pacifico, "#FFFFFF");
                    if (WelcomeUser.isTTS==1)
                        TTSManager.getInstance().speak("Minimum 6 characters!",
                            TextToSpeech.QUEUE_FLUSH,null,null);
                    etPasswordR.setError("Minimum 6 characters!");
                    etPasswordR.setBackgroundColor(Color.RED);
                    mp.start();
                    return;
                }
                stEmail=etEmailR.getText().toString();
                if (stEmail.equals("")) {
                    Toast.makeText(getContext(), "Write a e-mail -- a must!", Toast.LENGTH_SHORT).show();
                    CustomToast.showCustomToast(getContext(),
                            "Write a e-mail -- a must!",
                            R.drawable.problem, "#FF0000",
                            R.font.pacifico, "#FFFFFF");
                    if (WelcomeUser.isTTS==1)
                        TTSManager.getInstance().speak("Write a e-mail -- a must!",
                            TextToSpeech.QUEUE_FLUSH,null,null);
                    mp.start();
                    return;
                }
                if (isUserFound(stName, stPassword))  {
                    CustomToast.showCustomToast(getContext(),
                            "Unimpossible!",
                            R.drawable.problem, "#FF0000",
                            R.font.pacifico, "#FFFFFF");
                    if (WelcomeUser.isTTS==1)
                        TTSManager.getInstance().speak("Unimpossible!",
                            TextToSpeech.QUEUE_FLUSH,null,null);
                    mp.start();
                    return;
                }
                user=new User(stName,stEmail,stPassword);
                saveUser(user);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        return view;
    }

    private boolean isUserFound(String stName, String stPassword) {
        boolean flag=false;
        helperDB=new HelperDB(getContext());
        db=helperDB.getReadableDatabase();
        Cursor cursor=db.query(helperDB.USERS_TABLE,
                null,null,null,
                null,null,null);
        if (cursor.getCount()==0) {
            db.close();
            return flag;
        }
        cursor.moveToFirst();
        while (flag==false && !cursor.isAfterLast())  {
            String name=cursor.getString((int)cursor.getColumnIndex(helperDB.USER_NAME));
            String pass=cursor.getString((int)cursor.getColumnIndex(helperDB.USER_PASSWORD));
            if (name.equals(stName) && pass.equals(stPassword))  {
                String email=cursor.getString((int)cursor.getColumnIndex(helperDB.USER_MAIL));
                user=new User(name,email,pass);
                flag=true;
            }
            cursor.moveToNext();
        }
        db.close();
        return flag;
    }

    private void saveUser(User user) {
        Toast.makeText(requireContext(), ""+user.getUserName()+","+user.getUserPassword(), Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                helperDB = new HelperDB(getContext());
                db = helperDB.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(helperDB.USER_NAME, user.getUserName());
                contentValues.put(helperDB.USER_PASSWORD, user.getUserPassword());
                contentValues.put(helperDB.USER_MAIL, user.getUserEmail());
                db.insert(helperDB.USERS_TABLE, null, contentValues);
                db.close();

                if (getActivity() instanceof Activity) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomToast.showCustomToast(getContext(),
                                    "Ok, your data save",
                                    R.drawable.success, "#0000FF",
                                    R.font.pacifico, "#FFFFFF");
                            if (WelcomeUser.isTTS==1)
                                TTSManager.getInstance().speak("Ok, your data save",
                                    TextToSpeech.QUEUE_FLUSH,null,null);
                        }
                    });
                }
            }
        }).start();
    }
}