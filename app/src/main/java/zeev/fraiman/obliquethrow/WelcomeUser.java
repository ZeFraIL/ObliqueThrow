package zeev.fraiman.obliquethrow;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class WelcomeUser extends AppCompatActivity {

    Context context;
    FrameLayout flWU;
    LinearLayout LL_WU;
    Button bAboutAppWU,bSignUpWU,bLoginWU, bCreditWU, bFromNetWU;
    CreditFragment creditFragment;
    LoginFragment loginFragment;
    SignupFragment signupFragment;
    AboutAppFragment aboutAppFragment;
    InfoFromNetFragment infoFromNetFragment;
    User user;
    ImageView ivLogoWU;
    static int isTTS=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        initComponents();

        bLoginWU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragment=new LoginFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.flWU,loginFragment);
                ft.commit();
            }
        });

        bSignUpWU.setOnClickListener(v ->
        {
            signupFragment=new SignupFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flWU,signupFragment);
            ft.commit();
        });

        bCreditWU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creditFragment=new CreditFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.flWU,creditFragment);
                ft.commit();
            }
        });

        bAboutAppWU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutAppFragment=new AboutAppFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.flWU,aboutAppFragment);
                ft.commit();
            }
        });

        bFromNetWU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoFromNetFragment=new InfoFromNetFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.flWU,infoFromNetFragment);
                ft.commit();
            }
        });

        ivLogoWU.setOnClickListener(new View.OnClickListener() {
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

    private void initComponents() {
        context=this;
        bAboutAppWU=findViewById(R.id.bAboutAppWU);
        bCreditWU=findViewById(R.id.bCreditWU);
        bLoginWU=findViewById(R.id.bLoginWU);
        bSignUpWU=findViewById(R.id.bSignUpWU);
        bFromNetWU=findViewById(R.id.bFromNetWU);
        flWU=findViewById(R.id.flWU);
        LL_WU=findViewById(R.id.LL_WU);
        ivLogoWU=findViewById(R.id.ivLogoWU);
        user=new User("","","");
    }
}