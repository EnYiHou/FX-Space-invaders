package edu.vanier.ufo.engine;

import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Responsible for loading sound media to be played using an id or key. Contains
 * all sounds for use later.
 * <p/>
 * User: cdea
 */
public class SoundManager {

    static ExecutorService soundPool = Executors.newFixedThreadPool(20);
    static Map<String, AudioClip> soundEffectsMap = new HashMap<>();
    static Map<String, Media> gameMusic = new HashMap<>();

    /**
     * Constructor to create a simple thread pool.
     *
     * @param numberOfThreads - number of threads to use media players in the
     * map.
     */
    public static void setSoundPoolThread(int numberOfThreads) {
        soundPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    /**
     * Load a sound into a map to later be played based on the id.
     *
     * @param id - The identifier for a sound.
     * @param url - The url location of the media or audio resource. Usually in
     * src/main/resources directory.
     */
    public static void loadSoundEffects(String id, URL url) {
        AudioClip sound = new AudioClip(url.toExternalForm());

        soundEffectsMap.put(id, sound);
    }

    public static void loadGameMusic(String id, URL url) {
        Media music = new Media(url.toExternalForm());
        gameMusic.put(id, music);
    }

    /**
     * Lookup a name resource to play sound based on the id.
     *
     * @param id identifier for a sound to be played.
     */
    public static void playSound(final String id) {
        Runnable soundPlay = () -> {
            soundEffectsMap.get(id).play();
        };
        soundPool.execute(soundPlay);
    }

    public static MediaPlayer playGameMusic(final String id) {
        MediaPlayer player = new MediaPlayer(gameMusic.get(id));
        player.setOnEndOfMedia(() -> {
            player.seek(Duration.ZERO);
        });

        player.setOnReady(() -> {
            player.play();
        });
        player.setVolume(100);
        player.play();
        return player;
    }

    /**
     * Stop all threads and media players.
     */
    public static void shutdown() {
        soundPool.shutdown();
    }

}
