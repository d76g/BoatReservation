package com.boat.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Users implements Iterable<User> {
    ArrayList<User> users;

    public Users() {
        this.users = new ArrayList<>();
    }
    // add user
    public void addUser(User user) {
        this.users.add(user);
    }

    // is empty
    public boolean isEmpty() {
        return this.users.isEmpty();
    }
    // count
    public int count() {
        return this.users.size();
    }
    // iterate
    public Iterator<User> iterator() {
        return this.users.iterator();
    }

    // get user by id
    public User getUserById(String id) {
        for (User user : this.users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
    // remove user
    public void removeUser(User user) {
        this.users.remove(user);
    }

    // get last user id
    public String getLastUserId() {
        if (this.users.isEmpty()) {
            return "0";
        }
        return this.users.get(this.users.size() - 1).getId();
    }
    // increase new user id
    public String increaseUserId() {
        return String.valueOf(Integer.parseInt(getLastUserId()) + 1);
    }
}
