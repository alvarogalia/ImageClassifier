package com.alvarogalia.imageclassifier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
        Controller controller = new Controller();
        loader.setController(controller);
        loader.load();
        Scene scene = new Scene(loader.getRoot());
        primaryStage.setScene(scene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        /*final Button btnComenzar = (Button) root.lookup("#btnComenzar");
        final TextField txtSourcePath = (TextField) root.lookup("#txtSourcePath");
        final TextField txtPossitivePath = (TextField) root.lookup("#txtPossitivePath");
        final TextField txtExcludePath = (TextField) root.lookup("#txtExcludePath");
        final TextField txtNegativePath = (TextField) root.lookup("#txtNegativePath");

        btnComenzar.setVisible(false);
        btnComenzar.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            if(txtSourcePath.getText().isEmpty()||txtPossitivePath.getText().isEmpty()||txtExcludePath.getText().isEmpty()||txtNegativePath.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Not all paths were filled");
                alert.showAndWait();
            }else{
                String sourcePath = txtSourcePath.getText();
                final String possitivePath = txtPossitivePath.getText();
                String negativePath = txtNegativePath.getText();
                String excludePath = txtExcludePath.getText();

                int error = 0;

                final File fileSourcePath = new File(sourcePath);
                if(fileSourcePath.exists() && fileSourcePath.isDirectory()){
                    txtSourcePath.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                }else{
                    txtSourcePath.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    error++;
                }

                final File filePossitivePath = new File(possitivePath);
                if(filePossitivePath.exists() && filePossitivePath.isDirectory()){
                    txtPossitivePath.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                }else{
                    filePossitivePath.mkdirs();
                    if(filePossitivePath.exists()){
                        txtPossitivePath.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    }else{
                        txtPossitivePath.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                        error++;
                    }
                }
                final File fileNegativePath = new File(negativePath);
                if(fileNegativePath.exists() && fileNegativePath.isDirectory()){
                    txtNegativePath.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                }else{
                    fileNegativePath.mkdirs();
                    if(fileNegativePath.exists()){
                        txtNegativePath.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    }else{
                        txtNegativePath.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                        error++;
                    }
                }
                final File fileExcludePath = new File(excludePath);
                if(fileExcludePath.exists() && fileExcludePath.isDirectory()){
                    txtExcludePath.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                }else{
                    fileExcludePath.mkdirs();
                    if(fileExcludePath.exists()){
                        txtExcludePath.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    }else{
                        txtExcludePath.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                        error++;
                    }
                }

                if(error>0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Error creating path specified");
                    alert.setContentText("Please set a valid path to create folder if it not exists");
                    alert.showAndWait();
                }else{
                    final ImageView imgMain = (ImageView) root.lookup("#imgMain");
                    final File[] actualImage = new File[1];
                    root.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent keyEvent) {
                            if(keyEvent.getCode().getName().equals("1")){
                                try {
                                    FileUtils.moveFileToDirectory(actualImage[0], filePossitivePath, true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(keyEvent.getCode().getName().equals("2")){
                                try {
                                    FileUtils.moveFileToDirectory(actualImage[0], fileExcludePath, true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(keyEvent.getCode().getName().equals("3")){
                                try {
                                    FileUtils.moveFileToDirectory(actualImage[0], fileNegativePath, true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            BufferedImage bi = null;
                            try {
                                for (File child : fileSourcePath.listFiles()) {
                                    bi = getImage(child);
                                    actualImage[0] = child;
                                    break;
                                }
                                if(bi != null){
                                    imgMain.setImage(SwingFXUtils.toFXImage(bi, null));
                                }else{
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("ERROR");
                                    alert.setHeaderText("No more images2");
                                    alert.setContentText("There is no more images to classify in the specified path");
                                    alert.showAndWait();
                                }
                            } catch (IOException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("ERROR");
                                alert.setHeaderText("No more images1");
                                alert.setContentText(e.getMessage());
                                alert.showAndWait();
                            }

                        }
                    });

                    BufferedImage bi = null;
                    try {
                        for (File child : fileSourcePath.listFiles()) {
                            bi = getImage(child);
                            actualImage[0] = child;
                            break;
                        }
                        if(bi != null){
                            imgMain.setImage(SwingFXUtils.toFXImage(bi, null));
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("No more images3");
                            alert.setContentText("There is no more images to classify in the specified path");
                            alert.showAndWait();
                        }
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("No more images4");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }


                }
            }
        });*/

        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
