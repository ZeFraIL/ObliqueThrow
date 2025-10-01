package zeev.fraiman.obliquethrow;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTSManager {
    private static TTSManager instance;
    private TextToSpeech textToSpeech;

    private TTSManager() {}

    public static TTSManager getInstance() {
        if (instance == null) {
            instance = new TTSManager();
        }
        return instance;
    }

    public void init(Context context) {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
    }

    public void speak(String text, int queueMode, Bundle params, String utteranceId) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, queueMode, params, utteranceId);
        } else {
            //Log.e("TTSManager", "TextToSpeech not initialized!");
        }
    }
}
