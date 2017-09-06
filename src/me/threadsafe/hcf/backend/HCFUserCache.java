package me.threadsafe.hcf.backend;

import me.threadsafe.hcf.api.ConfigurationHelper;
import me.threadsafe.hcf.api.User;
import me.threadsafe.hcf.api.UserCache;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HCFUserCache implements UserCache {

    public List<User> users;

    public HCFUserCache(){
        users = new ArrayList<>();
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User create(UUID uuid, String name) {
        for(User user : users){
            if(user.getId().equals(uuid)) return user;
        }

        HCFUser user =  new HCFUser(uuid, name);
        users.add(user);

        return user;
    }

    @Override
    public User search(String name) {
        for(User user : users){
            if(user.getName().equalsIgnoreCase(name)) return user;
        }

        return null;
    }

    @Override
    public User search(UUID uuid) {
        for(User user : users){
            if(user.getId().equals(uuid)) return user;
        }

        return null;
    }

    @Override
    public void save() {
        YamlConfiguration config = ConfigurationHelper.create("users");

        for(User user : users) config.set(user.getId().toString(), user.getName());

        try {
            config.save(ConfigurationHelper.createFile("users"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
