package com.example.eback.service;

import com.example.eback.constans.UserRegistryCode;
import com.example.eback.dao.UserDAO;
import com.example.eback.entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Iterator;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    public String getRole(String username) {
        return userDAO.findByUsername(username).getRole();
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public boolean isExist(String username) {
        User user = userDAO.findByUsername(username);
        return null != user;
    }

    public UserRegistryCode register(User user) {

        String name = HtmlUtils.htmlEscape(user.getName());
        String role = "user";
        String password = user.getPassword();
        if (name.equals("")) {
            return UserRegistryCode.EMPTY_MSG;
        }
        if (userDAO.existsByName(name)) return UserRegistryCode.USER_EXISTS;
         // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();

        user.setSalt(salt);

        user.setPassword(encodedPassword);

        userDAO.save(user);

        return UserRegistryCode.REGISTRY_SUCCESS;
    }

    public User setPassword(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        userInDB.setSalt(salt);
        String encodedPassword = new SimpleHash("md5", user.getPassword(), salt, times).toString();
        userInDB.setPassword(encodedPassword);
        return userDAO.save(userInDB);
    }

    public void edit(User user) {
        User userDB = userDAO.findByUsername(user.getUsername());
        userDB.setRole(user.getRole());
        user.setName(user.getName());

        userDAO.save(userDB);

    }

    public User resetPassword(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        userInDB.setSalt(salt);
        String encodedPassword = new SimpleHash("md5", "00000000", salt, times).toString();
        userInDB.setPassword(encodedPassword);
        return userDAO.save(userInDB);
    }

    public void deleteById(int id) {
        userDAO.deleteById(id);
    }


    public List<User> list() {
        List<User> users = userDAO.findAll();
        Iterator<User> it = users.iterator();
        User user;
        while (it.hasNext()) {
            user = it.next();
            user.setPassword("");
            user.setSalt("");
        }
        return users;
    }

    public User get(int id) {
        User u = userDAO.findById(id).orElse(null);
        return u;
    }


}
