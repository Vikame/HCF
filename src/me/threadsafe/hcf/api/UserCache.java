package me.threadsafe.hcf.api;

import java.util.List;
import java.util.UUID;

public interface UserCache {

    List<User> getUsers();

    User create(UUID uuid, String name);

    User search(String name);
    User search(UUID uuid);

    void save();

}
