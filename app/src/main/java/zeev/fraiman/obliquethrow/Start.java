package zeev.fraiman.obliquethrow;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Start extends AppCompatActivity {

    Context context;
    ImageView ivStart;
    HelperDB helperDB;
    SQLiteDatabase db;
    CountDownTimer cdt;
    public static TextToSpeech totalTTS;
    int count=0;
    private Executor executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initComponents();

        ivStart.animate().alpha(0f).setDuration(3000).start();

        cdt=new CountDownTimer(3000,3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (count==0)  {
                    helperDB=new HelperDB(context);
                    db=helperDB.getWritableDatabase();
                    db.close();
                    count++;
                }
                else {
                    /*db=helperDB.getWritableDatabase();
                    long linksCount= DatabaseUtils.queryNumEntries(db,HelperDB.LINKS_TABLE);
                    if (linksCount==0)
                        executorService.execute(() -> startCopyLinks());
                    else
                        db.close();*/
                }
            }

            @Override
            public void onFinish() {
                Intent go=new Intent(context, WelcomeUser.class);
                startActivity(go);
            }
        }.start();
    }

    private void initComponents() {
        context=this;
        ivStart=findViewById(R.id.ivStart);
        helperDB=new HelperDB(context);
        db=helperDB.getWritableDatabase();
        db.close();
        executorService = Executors.newSingleThreadExecutor();
        db=helperDB.getWritableDatabase();
        long linksCount= DatabaseUtils.queryNumEntries(db,HelperDB.LINKS_TABLE);
        if (linksCount==0)
            executorService.execute(() -> startCopyLinks());
        else
            db.close();
        TTSManager.getInstance().init(context);
        /*totalTTS = new TextToSpeech(context,
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            totalTTS.setLanguage(Locale.US);

                        }
                    }
                });*/
        CustomToast.showCustomToast(context,
                "TWelcome\nTo\nOblique Throw!", R.drawable.physics_logo,
                "#00FF00",
                R.font.pacifico, "#FFFFFF");
    }

    private void startCopyLinks() {
        //helperDB=new HelperDB(context);
        //db=helperDB.getWritableDatabase();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.start_links);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String st1="",st2="",st3="";
            ArrayList<Link> a=new ArrayList<>();
            while ((st1 = reader.readLine()) != null) {
                st2=reader.readLine();
                st3=reader.readLine();
                ContentValues cv=new ContentValues();
                cv.put(helperDB.LINK_NAME, st1);
                cv.put(helperDB.LINK_ID, st2);
                cv.put(helperDB.LINK_TYPE, st3);
                a.add(new Link(st1,st2,st3));
                db.insert(helperDB.LINKS_TABLE,null,cv);
            }
            reader.close();
            db.close();
            runOnUiThread(() ->
                    Toast.makeText(context, "" + a.size(), Toast.LENGTH_SHORT).show()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}