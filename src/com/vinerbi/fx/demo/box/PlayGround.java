/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinerbi.fx.demo.box;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 * @author Alessio
 */
public class PlayGround extends Group {

    private final int sizeBox = 230;

    private Box[][] boxes;
 
    Media media;
    MediaPlayer player;
    MediaView view;

    public PlayGround() {
        
        File file = new File("video.mp4");

        try {
            media = new Media(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException ex) {
            Logger.getLogger(PlayGround.class.getName()).log(Level.SEVERE, null, ex);
        }
        player = new MediaPlayer(media);
        view = new MediaView(player);
        player.setAutoPlay(true);
    }

    private Box makeBox(double x, double y) {

        Rotate rotateY;
        Rotate rotateX;
        Box box = new Box(sizeBox, sizeBox, sizeBox);
        box.getTransforms().clear();
        rotateY = new Rotate(0, Rotate.Y_AXIS);
        rotateX = new Rotate(0, Rotate.X_AXIS);

        box.getTransforms().add(rotateY);
        box.getTransforms().add(rotateX);
        box.setTranslateX(x + 120);
        box.setTranslateY(y + 50);

        PhongMaterial m = new PhongMaterial(Color.WHITE);
        box.setMaterial(m);

        getChildren().add(box);
        Timeline time = new Timeline(new KeyFrame(Duration.seconds(10),
                new KeyValue(rotateY.angleProperty(), 360),
                new KeyValue(rotateX.angleProperty(), 360)));

        time.setCycleCount(-1);
        time.play();

        return box;
    }

    public void rebuildTexture(int w, int h, final Image imageSnap) {

        int numBoxH = (int) (w / sizeBox);
        int numBoxV = (int) (h / sizeBox);

        int currentX = 0;
        int currentY = 0;

        PixelReader reader = imageSnap.getPixelReader();

        for (int x = 0; x < numBoxH; x++) {
            for (int y = 0; y < numBoxV; y++) {

                WritableImage newImage = new WritableImage(reader, currentX, currentY, sizeBox, sizeBox);

                PhongMaterial phong = (PhongMaterial) boxes[x][y].getMaterial();

                phong.setDiffuseMap(newImage);

                currentY += sizeBox;
            }
            currentY = 0;
            currentX += sizeBox;
        }
    }

    public void setup(int w, int h) {

        int numBoxH = (int) (w / sizeBox);
        int numBoxV = (int) (h / sizeBox);

        boxes = new Box[numBoxH][numBoxV];

        int currentX = 0;
        int currentY = 0;
        for (int x = 0; x < numBoxH; x++) {
            for (int y = 0; y < numBoxV; y++) {

                boxes[x][y] = makeBox(currentX, currentY);
                currentY += sizeBox;
            }
            currentY = 0;
            currentX += sizeBox;
        }

        SnapshotParameters params = new SnapshotParameters();
        WritableImage imageSnap = new WritableImage(1280, 720);

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                view.snapshot(params, imageSnap);
                rebuildTexture(w, h, imageSnap);
            }
        };

        timer.start();

        Timeline timeCamera = new Timeline(new KeyFrame(Duration.seconds(5), new KeyValue(getScene().getCamera().translateZProperty(), 1000, Interpolator.EASE_BOTH)));

        timeCamera.setAutoReverse(true);
        timeCamera.setCycleCount(-1);
        timeCamera.play();
    }
}
