package com.alvarogalia.imageclassifier;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;

public class Controller {
    @FXML
    public Pane mainPane;
    @FXML
    public Button btnComenzar;
    @FXML
    public TextField txtSourcePath;
    @FXML
    public TextField txtPossitivePath;
    @FXML
    public TextField txtNegativePath;
    @FXML
    public TextField txtExcludePath;
    @FXML
    public ImageView imgMain;
    @FXML
    public ListView listNext;
    @FXML
    public Button btnDiscard;
    @FXML
    public ProgressBar progressbar;

    private int faltante;
    private int total;

    @FXML
    public void initialize() {
        btnDiscard.setVisible(false);
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
                    btnDiscard.setVisible(true);

                    txtExcludePath.setDisable(true);
                    txtNegativePath.setDisable(true);
                    txtPossitivePath.setDisable(true);
                    txtSourcePath.setDisable(true);

                    btnComenzar.setDisable(true);

                    final File[] actualImage = new File[1];
                    final int[] cant = new int[1];
                    mainPane.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
                        total = cant[0] + faltante;
                        double porcentaje =  ((cant[0]*100)/ total);
                        String texto = cant[0] + "/" + faltante + " " + (int)(porcentaje)+"%";
                        progressbar.setProgress(porcentaje/100);
                        btnComenzar.setText(texto);
                        if(keyEvent.getCode().getName().equals("1")){
                            try {
                                FileUtils.moveFileToDirectory(actualImage[0], filePossitivePath, true);
                                cant[0]++;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if(keyEvent.getCode().getName().equals("2")){
                            try {
                                FileUtils.moveFileToDirectory(actualImage[0], fileExcludePath, true);
                                cant[0]++;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if(keyEvent.getCode().getName().equals("3")){
                            try {
                                FileUtils.moveFileToDirectory(actualImage[0], fileNegativePath, true);
                                cant[0]++;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        BufferedImage bi = null;
                        listNext.getItems().clear();
                        try {
                            int first = 0;
                            faltante = fileSourcePath.listFiles().length;
                            for (File child : fileSourcePath.listFiles()) {
                                bi = getImage(child);
                                if(first==0){
                                    if(bi != null){
                                        actualImage[0] = child;
                                        imgMain.setImage(SwingFXUtils.toFXImage(bi, null));
                                    }else{
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("ERROR");
                                        alert.setHeaderText("No more images3");
                                        alert.setContentText("There is no more images to classify in the specified path");
                                        alert.showAndWait();
                                    }
                                }else{
                                    if(first <= 10){
                                        ImageView preview = new ImageView();
                                        preview.setImage(SwingFXUtils.toFXImage(resize(bi, 160,90), null));
                                        preview.setId(child.getAbsolutePath());
                                        listNext.getItems().add(preview);
                                    }else{
                                        break;
                                    }
                                }
                                first++;
                            }

                        } catch (IOException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("No more images4");
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
                        }
                    });

                    BufferedImage bi = null;
                    listNext.getItems().clear();
                    try {
                        int first = 0;
                        faltante = fileSourcePath.listFiles().length;
                        for (File child : fileSourcePath.listFiles()) {
                            bi = getImage(child);
                            if(first==0){
                                if(bi != null){
                                    actualImage[0] = child;
                                    imgMain.setImage(SwingFXUtils.toFXImage(bi, null));
                                }else{
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("ERROR");
                                    alert.setHeaderText("No more images3");
                                    alert.setContentText("There is no more images to classify in the specified path");
                                    alert.showAndWait();
                                }
                            }else{
                                if(first <= 10){
                                    ImageView preview = new ImageView();
                                    preview.setImage(SwingFXUtils.toFXImage(resize(bi, 160,90), null));
                                    preview.setId(child.getAbsolutePath());
                                    listNext.getItems().add(preview);
                                }else{
                                    break;
                                }
                            }
                            first++;
                        }

                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("No more images4");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }

                    btnDiscard.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            System.out.println("Descarta");
                            for(int i=0; i<10; i++){
                                ImageView imagen = (ImageView) listNext.getItems().get(i);
                                String id = imagen.getId();
                                System.out.println(id);
                                File file = new File(id);
                                try {
                                    FileUtils.moveFileToDirectory(file, fileNegativePath, true);
                                    cant[0]++;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            BufferedImage bi = null;
                            listNext.getItems().clear();
                            try {
                                int first = 0;
                                faltante = fileSourcePath.listFiles().length;
                                for (File child : fileSourcePath.listFiles()) {
                                    bi = getImage(child);
                                    if(first==0){
                                        if(bi != null){
                                            actualImage[0] = child;
                                            imgMain.setImage(SwingFXUtils.toFXImage(bi, null));
                                        }else{
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("ERROR");
                                            alert.setHeaderText("No more images3");
                                            alert.setContentText("There is no more images to classify in the specified path");
                                            alert.showAndWait();
                                        }
                                    }else{
                                        if(first <= 10){
                                            ImageView preview = new ImageView();
                                            preview.setImage(SwingFXUtils.toFXImage(resize(bi, 160,90), null));
                                            preview.setId(child.getAbsolutePath());
                                            listNext.getItems().add(preview);
                                        }else{
                                            break;
                                        }
                                    }
                                    first++;
                                }

                            } catch (IOException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("ERROR");
                                alert.setHeaderText("No more images4");
                                alert.setContentText(e.getMessage());
                                alert.showAndWait();
                            }
                            total = cant[0] + faltante;
                            double porcentaje =  ((cant[0]*100)/ total);
                            String texto = cant[0] + "/" + faltante + " " + (int)(porcentaje)+"%";
                            progressbar.setProgress(porcentaje/100);
                            btnComenzar.setText(texto);
                        }
                    });
                }
            }
        });
    }

    public BufferedImage getImage(File child) throws IOException {
        if(child.getName().toUpperCase().endsWith(".JPG")||
                child.getName().toUpperCase().endsWith(".PNG")||
                child.getName().toUpperCase().endsWith(".JPEG")||
                child.getName().toUpperCase().endsWith(".BMP")){


            BufferedImage in = ImageIO.read(child);
            BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newImage.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();
            return in;
        }
        return null;
    }
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}