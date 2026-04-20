package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;

public class CprFragment extends Fragment {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private ToneGenerator tone;
    private Vibrator vibrator;

    private boolean running = false;
    private int bpm = 110;
    private int compressions = 0;

    private View pulseCircle;
    private TextView compressionCount;
    private TextView bpmText;
    private MaterialButton startStopButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cpr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);

        pulseCircle = view.findViewById(R.id.pulse_circle);
        compressionCount = view.findViewById(R.id.compression_count);
        bpmText = view.findViewById(R.id.bpm_text);
        startStopButton = view.findViewById(R.id.start_stop_button);
        Slider slider = view.findViewById(R.id.bpm_slider);

        slider.addOnChangeListener((s, value, fromUser) -> {
            bpm = (int) value;
            bpmText.setText(bpm + " BPM");
        });

        startStopButton.setOnClickListener(v -> {
            if (running) stop();
            else start();
        });
    }

    private void start() {
        running = true;
        compressions = 0;
        compressionCount.setText("0");
        startStopButton.setText("STOP");
        try {
            tone = new ToneGenerator(AudioManager.STREAM_ALARM, 80);
        } catch (Exception ignored) {}
        handler.post(beat);
    }

    private void stop() {
        running = false;
        startStopButton.setText("START");
        handler.removeCallbacksAndMessages(null);
        if (tone != null) {
            tone.release();
            tone = null;
        }
        pulseCircle.clearAnimation();
    }

    private final Runnable beat = new Runnable() {
        @Override
        public void run() {
            if (!running) return;

            if (tone != null) tone.startTone(ToneGenerator.TONE_PROP_BEEP, 60);

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //noinspection deprecation
                    vibrator.vibrate(40);
                }
            }

            pulseCircle.animate()
                    .scaleX(1.2f).scaleY(1.2f)
                    .setDuration(80)
                    .withEndAction(() -> pulseCircle.animate()
                            .scaleX(1f).scaleY(1f)
                            .setDuration(120)
                            .start())
                    .start();

            compressions++;
            compressionCount.setText(String.valueOf(compressions));

            handler.postDelayed(this, 60_000L / bpm);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (running) stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tone != null) tone.release();
    }
}