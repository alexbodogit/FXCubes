/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinerbi.fx.demo.box;

import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Alessio
 */
public class CubeDemo extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        PlayGround root = new PlayGround();
        
        
        Scene scene = new Scene(root,1280,720,Color.BLACK);
        
        scene.setCamera(new PerspectiveCamera());
        
        root.setup(1280,720);
        
        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
