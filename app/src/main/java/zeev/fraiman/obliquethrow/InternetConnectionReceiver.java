package zeev.fraiman.obliquethrow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class InternetConnectionReceiver extends BroadcastReceiver {

    private View targetView;

    public InternetConnectionReceiver() {
        super();
    }

    public InternetConnectionReceiver(View targetView) {
        this.targetView = targetView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            boolean isInternetAvailable = capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));

            if (targetView!=null)
                targetView.setEnabled(isInternetAvailable);

            if (!isInternetAvailable) {
                CustomToast.showCustomToast(context,"Not found connection to Internet!",
                        R.drawable.problem,"#FF0000",R.font.pacifico,"#FFFFFF");
                targetView.setEnabled(isInternetAvailable);
            }
        }
    }
}
