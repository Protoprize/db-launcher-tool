package org.p.launcher;

import com.google.gson.Gson;
import org.apache.tools.ant.types.Commandline;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void replaceLauncher(String dbPath, String launcherPath, String launcherType,
                                       String launcherArgs, Boolean cli) throws IOException {
        String manualLauncher = launcherPath;
        if (launcherPath.isEmpty()) {
            String userHome = System.getProperty("user.home");

            switch (launcherType) {
                case "HDOS":
                    File hdosPath = new File(userHome, "AppData/Local/HDOS/hdos-launcher.jar");

                    if (hdosPath.exists()) {
                        manualLauncher = hdosPath.getAbsolutePath();
                    } else {
                        throw new IOException("HDOS not found");
                    }
                    break;

                case "RuneLite":
                    File runeLitePath = new File(userHome, "AppData/Local/RuneLite/RuneLite.jar");

                    if (runeLitePath.exists()) {
                            manualLauncher = runeLitePath.getAbsolutePath();
                    } else {
                        throw new IOException("RuneLite not found");
                    }
                    break;

                default:
                    // TODO: Add warning box
            }

            try {
                updateFilesLauncher(cli, dbPath, manualLauncher, launcherArgs);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void updateFilesLauncher(Boolean cli, String source, String dest, String cliArgs)
            throws IOException, URISyntaxException {
        Gson gson = new Gson();
        if (cli) {

            source = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI().getPath()).getAbsoluteFile().toString().replace("db-launcher-tool.jar",
                    "db-cli-forwarder.jar");

            File sourceFileObj = new File(source);
            File destFileObj = new File(dest);

            String destFilename = dest.substring(dest.lastIndexOf("\\") + 1);
            String configJsonPath = dest.replace(destFilename, "config.json");
            String dbconfigJsonPath = dest.replace(destFilename, "db_config.txt");

            Files.copy(sourceFileObj.toPath(), destFileObj.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            String configJson = gson.toJson( new Configuration(
                    List.of(destFilename),
                    "org.p.Main",
                    List.of()));

            File configJsonFile = new File(configJsonPath);
            Files.write(configJsonFile.toPath(), configJson.getBytes());

            FileWriter writer = new FileWriter(dbconfigJsonPath);
            writer.write(cliArgs);
            writer.close();
        } else {
            File sourceFileObj = new File(source);
            File destFileObj = new File(dest);
            String destFilename = dest.substring(dest.lastIndexOf("\\") + 1);
            String configJson = gson.toJson( new Configuration(
                    List.of(destFilename),
                    "org.dreambot.launcher.DreamLauncher",
                    List.of()));
            Files.copy(sourceFileObj.toPath(), destFileObj.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            String configJsonPath = dest.replace(destFilename, "config.json");

            FileWriter writer = new FileWriter(configJsonPath);
            writer.write(configJson);
            writer.close();

        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AppGui gui = new AppGui();
                gui.run();
            }
        });
    }
}