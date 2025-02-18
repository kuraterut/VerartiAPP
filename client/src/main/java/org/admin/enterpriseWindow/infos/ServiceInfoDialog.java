package org.admin.enterpriseWindow.infos;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.getRequests.GetAdmin;
import org.admin.connection.getRequests.GetService;
import org.admin.utils.AdminInfo;
import org.admin.utils.ServiceInfo;

public class ServiceInfoDialog extends Main {
    public static void show(Long id){
        ServiceInfo service = GetService.getById(token, id);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация об Услуге");

        VBox root = new VBox();

        Scene dialogScene = new Scene(root, 1500, 800);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
