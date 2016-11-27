/**
 * @author: kris chenyang su
 * @data: 11/23/2016
 * 
 * A simple web browser based on Mr. Lee Stemkoski's previous work.
 * Functions as any web browser.
 * features: forward button, backward button, homebutton, zoom-in/out button, set home page and favorite pages
 * 
 */

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*; 
import javafx.animation.*;
import javafx.geometry.*;
import java.io.*;
import java.util.*;
import java.lang.StringBuffer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.scene.web.*;

public class browser extends Application 
{
    public static void main(String[] args) 
    {
        // Automatic VM reset, thanks to Joseph Rachmuth.
        try
        {
            launch(args);
            System.exit(0);
        }
        catch (Exception error)
        {
            error.printStackTrace();
            System.exit(0);
        }
    }

    String defaultURL = "http://www.google.com";

    public void start(Stage mainStage) 
    {
        BorderPane root = new BorderPane();

        Scene mainScene = new Scene(root);
        mainStage.setScene(mainScene);
        mainStage.getIcons().add(new Image("assets/title.png"));

        VBox vb = new VBox();
        vb.setSpacing(2);

        HBox hb = new HBox();
        hb.setPadding( new Insets(4) );
        hb.setSpacing(4);

        System.setProperty("jsse.enableSNIExtension", "false");

        WebView browser = new WebView();
        //browser.setPrefSize(800,600);

        WebEngine webEngine = browser.getEngine();
        webEngine.load(defaultURL);

        TextField urlText = new TextField(defaultURL);
        urlText.setOnKeyPressed(new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent keyEvent)
                {
                    if(keyEvent.getCode() == KeyCode.ENTER) {
                        String url = urlText.getText();
                        if (url.startsWith("http://") != true)
                        {
                            url = "http://" + url;
                        }
                        webEngine.load( url );
                    }
                }
            }
        );

        //go to website exist in the textfield
        Button urlButton = new Button();
        urlButton.setGraphic( new ImageView(new Image("assets/go.png",16,16,true,true)));
        urlButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    String url = urlText.getText();

                    if (url.startsWith("http://") != true)
                    {
                        url = "http://" + url;
                    }
                    webEngine.load( url );

                }
            }
        );

        //back
        Button backButton = new Button();
        backButton.setGraphic( new ImageView(new Image("assets/backward.png",16,16,true,true)));
        backButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    // this executes JavaScript code!
                    webEngine.executeScript( "history.back()" );
                }
            }
        );

        //forward
        Button forwardButton = new Button();
        forwardButton.setGraphic( new ImageView(new Image("assets/forward.png",16,16,true,true)));
        forwardButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    // this executes JavaScript code!
                    webEngine.executeScript( "history.forward()" );
                }
            }
        );

        Button homeButton = new Button();
        homeButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    webEngine.load( defaultURL);
                }
            }
        );
        homeButton.setGraphic( new ImageView(new Image("assets/home.png",16,16,true,true)));

        Button inButton = new Button();
        inButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    browser.setZoom(browser.getZoom() * 1.15);
                }
            }
        );
        inButton.setGraphic( new ImageView(new Image("assets/zoomIn.png",16,16,true,true)));
        
        Button outButton = new Button();
        outButton.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent e)
                {
                    browser.setZoom(browser.getZoom() / 1.15);
                }
            }
        );
        outButton.setGraphic( new ImageView(new Image("assets/zoomOut.png",16,16,true,true)));

        HBox.setHgrow(urlText, Priority.ALWAYS);

        hb.getChildren().addAll(backButton, forwardButton, homeButton, urlButton, inButton, outButton, urlText);

        //menu bar
        MenuBar menuBar = new MenuBar();
        Menu menuHome = new Menu("Home");
        menuBar.getMenus().add(menuHome);

        //menuitem favorites
        Menu favorites = new Menu("Favorites");
        favorites.setGraphic( new ImageView(new Image("assets/star.png",16,16,true,true)));
        menuHome.getItems().add(favorites);

        MenuItem first = new MenuItem("Adelphi");
        first.setGraphic( new ImageView(new Image("assets/adelphi.png",16,16,true,true)));
        favorites.getItems().add(first);
        first.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event){
                    webEngine.load("http://www.adelphi.edu");
                }
            }
        );

        MenuItem second = new MenuItem("niconico");
        second.setGraphic( new ImageView(new Image("assets/niconico.png",16,16,true,true)));
        favorites.getItems().add(second);
        second.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event){
                    webEngine.load("http://www.nicovideo.jp");
                }
            }
        );

        MenuItem third = new MenuItem("ProfStemkoski");
        third.setGraphic( new ImageView(new Image("assets/twitter.png",16,16,true,true)));
        favorites.getItems().add(third);
        third.setOnAction(
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event){
                    webEngine.load("https://twitter.com/ProfStemkoski");
                }
            }
        );

        //menuitem set homepage
        MenuItem setHomePage = new MenuItem("Set Home Page");
        setHomePage.setGraphic( new ImageView(new Image("assets/setting.png",16,16,true,true)));
        setHomePage.setOnAction( 
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    TextInputDialog homeDialog = new TextInputDialog();
                    homeDialog.setTitle("New Home Page");
                    homeDialog.setHeaderText(null);
                    homeDialog.setContentText("Please enter the web address:");

                    try{
                        Optional<String> address = homeDialog.showAndWait();
                        //if user didn't input anything, send alert
                        //no matter what the user typed in, or valid, save it as default url
                        while(address.get() == null || address.get().trim().isEmpty()) {
                            Alert infoAlert = new Alert(AlertType.INFORMATION);
                            infoAlert.setTitle("Warning");
                            infoAlert.setHeaderText(null); 
                            infoAlert.setContentText("Home page cannot be empty!");
                            infoAlert.showAndWait();

                            address = homeDialog.showAndWait();
                        }

                        String s = address.get();
                        //append 'http://' if the input doesn't has it
                        if (s.startsWith("http://") != true)
                        {
                            s = "http://" + s;
                        }
                        defaultURL = s;
                    }
                    catch(Exception e){
                        Alert infoAlert2 = new Alert(AlertType.INFORMATION);
                        infoAlert2.setTitle("Warning");
                        infoAlert2.setHeaderText(null); 
                        infoAlert2.setContentText("Home page did not change.");
                        infoAlert2.showAndWait();
                    }
                }
            }
        );
        menuHome.getItems().add(setHomePage);

        //menuitem about
        MenuItem about = new MenuItem("About this program");
        about.setGraphic( new ImageView(new Image("assets/about.png",16,16,true,true)));
        about.setOnAction( 
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    Alert about = new Alert(AlertType.INFORMATION);
                    about.setTitle("About this program");
                    about.setGraphic(new ImageView(new Image("assets/info.png", 64, 64, true, true)));
                    about.setHeaderText(null);

                    String message = "Created by Kris Su.\n"
                                      + "Based on Mr. Lee Stemkoski's work.";
                    about.setContentText(message);
                    about.showAndWait();
                }
            }
        );
        menuHome.getItems().add(about);

        //menuitem quit, does what it sounds like
        MenuItem quit = new MenuItem("Quit");
        quit.setGraphic( new ImageView(new Image("assets/quit.png",16,16,true,true)));
        quit.setOnAction( 
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    mainStage.close();
                }
            }
        );
        menuHome.getItems().add(quit);

        vb.getChildren().addAll(menuBar, hb);

        webEngine.getLoadWorker().stateProperty().addListener(
            new ChangeListener() 
            {
                public void changed(ObservableValue ov, Object oldValue, Object newValue) 
                {
                    //System.out.println("WebView state changed");
                    String title = webEngine.getTitle();
                    String location = webEngine.getLocation();
                    if (title != null)
                    {
                        mainStage.setTitle(title);
                    }
                    urlText.setText(location);
                }
            }
        );

        root.setTop(vb);
        root.setCenter(browser);

        // custom code above --------------------------------------------
        mainStage.show();
    }
}