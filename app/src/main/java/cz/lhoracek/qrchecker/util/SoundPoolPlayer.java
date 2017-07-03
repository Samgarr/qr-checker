package cz.lhoracek.qrchecker.util;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.RawRes;
import android.util.SparseArray;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.di.ApplicationContext;

public class SoundPoolPlayer {
    private SoundPool mShortPlayer = null;
    private SparseArray<Integer> mSounds = new SparseArray<>();
    private Context context;

    @Inject
    public SoundPoolPlayer(@ApplicationContext Context context) {
        this.context = context;
        this.mShortPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
    }

    public void loadSound(@RawRes int sound) {
        mSounds.put(sound, this.mShortPlayer.load(context, sound, 1));
    }

    public void unloadSound(@RawRes int sound) {
        mShortPlayer.unload(mSounds.get(sound));
        mSounds.remove(sound);
    }

    public void playShortResource(int piResource) {
        int iSoundId = mSounds.get(piResource);
        this.mShortPlayer.play(iSoundId, 0.99f, 0.99f, 0, 0, 1);
    }

    // Cleanup
    public void release() {
        // Cleanup
        this.mShortPlayer.release();
        this.mShortPlayer = null;
    }
}