package ai.classifai.ui.selector.project;

import ai.classifai.ui.DesktopUI;
import ai.classifai.ui.component.SelectionWindow;
import ai.classifai.ui.enums.SelectionWindowStatus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ImageFileSelector extends SelectionWindow {

    public ImageFileSelector(DesktopUI ui) {
        super(ui);
    }

    @Getter private final List<String> imagePathList = new ArrayList<>();
    @Getter private final List<String> imageDirectoryList = new ArrayList<>();

    private static final FileNameExtensionFilter imgFilter = new FileNameExtensionFilter(
            "Image Files", "jpeg", "jpg", "png", "bmp", "webp");

    // Check whether the selected is image file or folder
    private boolean checkIsFile(File[] files)
    {
        List<Boolean> list = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                list.add(file.isFile());
            }
        }

        return Collections.frequency(list, true) == list.size();
    }

    private void prepareDataList(File[] imageFile)
    {
        if(imageFile != null && checkIsFile(imageFile)) {
            List<String> pathList = Arrays.stream(imageFile)
                    .map(File::getAbsolutePath)
                    .filter(absolutePath -> !imagePathList.contains(absolutePath))
                    .collect(Collectors.toList());

            imagePathList.addAll(pathList);
        }

        if(imageFile != null && !checkIsFile(imageFile)) {
            List<String> directoryList = Arrays.stream(imageFile)
                    .map(File::getAbsolutePath)
                    .filter(absolutePath -> !imageDirectoryList.contains(absolutePath))
                    .collect(Collectors.toList());

            imageDirectoryList.addAll(directoryList);
        }
    }

    public void run()
    {
        try
        {
            EventQueue.invokeLater(() -> {

                if (windowStatus.equals(SelectionWindowStatus.WINDOW_CLOSE))
                {
                    windowStatus = SelectionWindowStatus.WINDOW_OPEN;

                    File[] imageFile;

                    JFrame frame = ui.getFrameAtMousePointer();
                    String title = "Select Image File (.jpeg, .jpg, .png, .webp, .bmp) or Image Folder";
                    JFileChooser chooser = initChooser(JFileChooser.FILES_AND_DIRECTORIES, title);
                    chooser.setFileFilter(imgFilter);
                    chooser.setMultiSelectionEnabled(true);

                    //Important: prevent Welcome Console from popping out
                    ui.ensureWelcomeLauncherStaysInBackground();

                    int res = chooser.showOpenDialog(frame);
                    frame.dispose();

                    if (res == JFileChooser.APPROVE_OPTION)
                    {
                        imageFile = chooser.getSelectedFiles();
                        prepareDataList(imageFile);
                    }
                    else
                    {
                        log.debug("Operation of import images aborted");
                        imagePathList.clear();
                        imageDirectoryList.clear();
                    }

                    windowStatus = SelectionWindowStatus.WINDOW_CLOSE;
                }
                else
                {
                    showAbortImportPopup();
                }
            });
        }
        catch (Exception e)
        {
            log.info("ImageFileSelector failed to open", e);
        }
    }

}
