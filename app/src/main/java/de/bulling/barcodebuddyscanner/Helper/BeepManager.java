package de.bulling.barcodebuddyscanner.Helper;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

/**
 * Manages beeps and vibrations.
 */
public final class BeepManager {

	private static final String TAG = BeepManager.class.getSimpleName();

	private static final long VIBRATE_DURATION = 200L;

	private final Context context;

	private boolean beepEnabled    = true;
	private boolean vibrateEnabled = false;
	private float   beepVolume     = 0.25f;

	private SoundPool soundPool;
	private int       beepSound;

	public BeepManager(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// We do not keep a reference to the Activity itself, to prevent leaks
		this.context = activity.getApplicationContext();

		AudioAttributes attributes = new AudioAttributes.Builder()
				.setUsage(AudioAttributes.USAGE_MEDIA)
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.build();
		soundPool = new SoundPool.Builder()
				.setAudioAttributes(attributes)
				.build();

		beepSound = soundPool.load(context, com.google.zxing.client.android.R.raw.zxing_beep, 1);
	}

	public boolean isBeepEnabled() {
		return beepEnabled;
	}

	/**
	 * Call updatePrefs() after setting this.
	 * <p>
	 * If the device is in silent mode, it will not beep.
	 *
	 * @param beepEnabled true to enable beep
	 */
	public void setBeepEnabled(boolean beepEnabled) {
		this.beepEnabled = beepEnabled;
	}


	public void setBeepVolume(float volume) {
		this.beepVolume = volume;
	}


	public boolean isVibrateEnabled() {
		return vibrateEnabled;
	}

	/**
	 * Call updatePrefs() after setting this.
	 *
	 * @param vibrateEnabled true to enable vibrate
	 */
	public void setVibrateEnabled(boolean vibrateEnabled) {
		this.vibrateEnabled = vibrateEnabled;
	}

	public synchronized void playBeepSoundAndVibrate() {
		if (beepEnabled) {
			playBeepSound();
		}
		if (vibrateEnabled) {
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}


	public void playBeepSound() {
		soundPool.play(beepSound, beepVolume, beepVolume, 1, 0, 1);
	}
}
