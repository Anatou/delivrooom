package fr.delivrooom.adapter.in.javafxgui.panes.sidebar;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Bottom fixed section with progress bar and GO button.
 * The progress bar is shown when loading, and the GO button is shown when not loading.
 */
public class BottomSection extends VBox {

    private final AppController controller;
    private final ProgressBar progressBar;
    private final Button goButton;

    public BottomSection(AppController controller) {
        super(10);
        setPadding(new Insets(10));
        this.controller = controller;

        setAlignment(Pos.CENTER);

        // Progress bar (shown when loading)
        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setProgress(0.75); // Indeterminate progress
//        progressBar.visibleProperty().bind(loadingProperty);

        // GO button (shown when not loading)
        goButton = new Button("GO");
        goButton.setGraphic(new FontIcon(FontAwesomeSolid.PLAY));
        goButton.setMaxWidth(Double.MAX_VALUE);
        goButton.getStyleClass().addAll("accent", "large");
//        goButton.visibleProperty().bind(loadingProperty.not());

        goButton.setOnAction(e -> {

        });

        getChildren().addAll(progressBar, goButton);
    }

}
