package me.threadsafe.hcf.api;

import me.threadsafe.hcf.HCF;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigurationHelper {

    public static YamlConfiguration create(String name){
        return create(createFile(name));
    }

    public static YamlConfiguration create(File file){
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public static File createFile(String name){
        return new File(HCF.getInstance().getDataFolder(), name + ".yml");
    }

}
