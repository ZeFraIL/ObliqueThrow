package zeev.fraiman.obliquethrow;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.io.*;

public class BackupJobService extends JobService {
    private static final String TAG = "BackupJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        new Thread(() -> {
            backupDatabase();
            jobFinished(params, false);
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void backupDatabase() {
        File dbFile = new File(getFilesDir(), "info.db");
        File backupDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Backup");
        File backupFile = new File(backupDir, "info_backup.db");

        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        try (InputStream in = new FileInputStream(dbFile);
             OutputStream out = new FileOutputStream(backupFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            CustomToast.showCustomToast(getApplicationContext(),
                    "Backup successful: " + backupFile.getAbsolutePath(),
                    R.drawable.success, "#4CAF50", R.font.pacifico, "#FFFFFF");
            if (WelcomeUser.isTTS==1)
                TTSManager.getInstance().speak("Backup successful: " + backupFile.getAbsolutePath(),
                    TextToSpeech.QUEUE_FLUSH,null,null);
        } catch (IOException e) {
            CustomToast.showCustomToast(getApplicationContext(),
                    "Backup failed",
                    R.drawable.problem, "#FF0000", R.font.pacifico, "#FFFFFF");
            if (WelcomeUser.isTTS==1)
                TTSManager.getInstance().speak("Backup failed",
                    TextToSpeech.QUEUE_FLUSH,null,null);
        }
    }
}
