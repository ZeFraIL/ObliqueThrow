package zeev.fraiman.obliquethrow;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    Context context;
    private Button bInputData, bAnimation,
            bTrajectory, bPoints, bViewExercises,
            bConsepts, bUnits, bFormulas, bSelfStudy;

    ImageView ivLogoMA;
    User user;
    Intent takeit;
    static int isTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initComponents();
        
        bInputData.setOnClickListener(view -> loadFragment(new InputDataFragment()));
        bAnimation.setOnClickListener(view -> loadFragment(new AnimationFragment()));
        bTrajectory.setOnClickListener(view -> loadFragment(new TrajectoryFragment()));
        bPoints.setOnClickListener(view -> loadFragment(new PointsFragment()));
        bViewExercises.setOnClickListener(view -> loadFragment(new ViewExercisesFragment()));
        bConsepts.setOnClickListener(view -> loadFragment(new ConceptsFragment()));
        bUnits.setOnClickListener(view -> loadFragment(new UnitsFragment()));
        bFormulas.setOnClickListener(view -> loadFragment(new FormulasFragment()));
        bSelfStudy.setOnClickListener(view -> loadFragment(new SelfStudyFragment()));

        loadFragment(new InputDataFragment());

        ivLogoMA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.all_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.ttsOn) {
                            Toast.makeText(context, "TextToSpeech ON", Toast.LENGTH_SHORT).show();
                            isTTS = 1;
                        }
                        if (id == R.id.ttsOff) {
                            Toast.makeText(context, "TextToSpeech OFF", Toast.LENGTH_SHORT).show();
                            isTTS = 0;
                        }
                        if (id == R.id.guide) {
                            Toast.makeText(context, "Go to guide", Toast.LENGTH_SHORT).show();
                            AboutAppFragment aboutAppFragment = new AboutAppFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, aboutAppFragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                        if (id == R.id.credits) {
                            Toast.makeText(context, "Credits", Toast.LENGTH_SHORT).show();
                            CreditFragment creditsFragment = new CreditFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, creditsFragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                        if (id == R.id.exit) {
                            Toast.makeText(context, "Exiting app", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                        }
                        if (id == R.id.back) {
                            Toast.makeText(context, "Going back", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if (id == R.id.backup_on) {
                            Toast.makeText(context, "Backup ON", Toast.LENGTH_SHORT).show();
                            JobSchedulerHelper.scheduleBackup(context);
                        }
                        if (id == R.id.backup_off) {
                            Toast.makeText(context, "Backup OFF", Toast.LENGTH_SHORT).show();
                            JobSchedulerHelper.cancelBackup(context);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initComponents() {
        context=this;
        bInputData = findViewById(R.id.bInputData);
        bAnimation = findViewById(R.id.bAnimation);
        bTrajectory = findViewById(R.id.bTrajectory);
        bPoints = findViewById(R.id.bPoints);
        bViewExercises = findViewById(R.id.bViewExercises);
        bConsepts = findViewById(R.id.bConsepts);
        bUnits = findViewById(R.id.bUnits);
        bFormulas = findViewById(R.id.bFormulas);
        bSelfStudy = findViewById(R.id.bSelfStudy);
        ivLogoMA = findViewById(R.id.ivLogoMA);
        takeit=getIntent();
        user=(User) takeit.getSerializableExtra("user");
        isTTS=takeit.getIntExtra("isTTS",1);
        CustomToast.showCustomToast(context,"ST="+isTTS,11,"#FFFFFF",R.font.pacifico,"#000000");
    }
}
