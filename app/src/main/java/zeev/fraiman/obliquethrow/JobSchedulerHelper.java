package zeev.fraiman.obliquethrow;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class JobSchedulerHelper {
    private static final int JOB_ID = 123;

    public static void scheduleBackup(Context context) {
        ComponentName componentName = new ComponentName(context, BackupJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiresCharging(false)
                .setPersisted(true)
                .setPeriodic(24 * 60 * 60 * 1000)
                .build();

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = jobScheduler.schedule(jobInfo);

        if (result == JobScheduler.RESULT_SUCCESS) {
            CustomToast.showCustomToast(context.getApplicationContext(),
                    "Backup job scheduled successfully!",
                    R.drawable.success, "#4CAF50", R.font.pacifico, "#FFFFFF");
        } else {
            Log.e("JobSchedulerHelper", "Backup job scheduling failed.");
            CustomToast.showCustomToast(context.getApplicationContext(),
                    "Backup job scheduling failed.",
                    R.drawable.problem, "#FF0000", R.font.pacifico, "#FFFFFF");
        }
    }

    public static void cancelBackup(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
        CustomToast.showCustomToast(context.getApplicationContext(),
                "Backup job canceled.",
                R.drawable.problem, "#00FF00", R.font.pacifico, "#FFFFFF");
    }
}
