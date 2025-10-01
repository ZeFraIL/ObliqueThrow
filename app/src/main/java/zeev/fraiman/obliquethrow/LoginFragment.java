package zeev.fraiman.obliquethrow;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    Button blogin;
    EditText etUsernameL, etPasswordL;
    String stUsername="", stPassword="";
    HelperDB helperDB;
    SQLiteDatabase db;
    User user;
    MediaPlayer mp;
    int isTTS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_login, container, false);

        etUsernameL=view.findViewById(R.id.etUsernameL);
        etPasswordL=view.findViewById(R.id.etPasswordL);
        blogin=view.findViewById(R.id.bLogin);
        helperDB=new HelperDB(getContext());
        mp=MediaPlayer.create(getContext(),R.raw.alarm);
        isTTS=WelcomeUser.isTTS;

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stUsername=etUsernameL.getText().toString();
                if (stUsername.equals("")) {
                    CustomToast.showCustomToast(getContext(),
                            "Write a name -- a must!",
                            R.drawable.problem, "#FF0000",
                            R.font.pacifico, "#FFFFFF");
                    etUsernameL.setError("Write user name here!");
                    if (isTTS==1)TTSManager.getInstance().speak("Write a name -- a must!",
                            TextToSpeech.QUEUE_FLUSH,null,null);
                    mp.start();
                    return;
                }
                stPassword=etPasswordL.getText().toString();
                if (stPassword.equals("")) {
                    CustomToast.showCustomToast(getContext(),
                            "Write a password -- a must!",
                            R.drawable.problem, "#FF0000",
                            R.font.pacifico, "#FFFFFF");
                    if (isTTS==1)TTSManager.getInstance().speak("Write a password -- a must!",
                            TextToSpeech.QUEUE_FLUSH,null,null);
                    mp.start();
                    return;
                }
                if (stPassword.length()<6) {
                    Toast.makeText(getContext(), "Minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    if (isTTS==1)TTSManager.getInstance().speak("Minimum 6 characters!",
                            TextToSpeech.QUEUE_FLUSH,null,null);
                    CustomToast.showCustomToast(getContext(),
                            "Minimum 6 characters!",
                            R.drawable.problem, "#FF0000",
                            R.font.pacifico, "#FFFFFF");
                    mp.start();
                    return;
                }
                if (!isUserFound(stUsername, stPassword))  {
                    CustomToast.showCustomToast(getContext(),
                            "UNIMPOSSIBLE!",
                            R.drawable.problem, "#FF0000",
                            R.font.pacifico, "#FFFFFF");
                    if (isTTS==1)
                        TTSManager.getInstance().speak("UNIMPOSSIBLE!",
                            TextToSpeech.QUEUE_FLUSH,null,null);
                    mp.start();
                    return;
                }
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("user",user);
                intent.putExtra("isTTS",isTTS);
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
            String uname=cursor.getString((int)cursor.getColumnIndex(helperDB.USER_NAME));
            String upass=cursor.getString((int)cursor.getColumnIndex(helperDB.USER_PASSWORD));
            if (uname.equals(stName) && upass.equals(stPassword))  {
                String umail=cursor.getString((int)cursor.getColumnIndex(helperDB.USER_MAIL));
                user=new User(stName,umail,stPassword);
                flag=true;
            }
            cursor.moveToNext();
        }
        db.close();
        return flag;
    }
}