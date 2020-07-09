package nathan.luka.myseries.controller;


import nathan.luka.myseries.dataprovider.DataProvider;
import nathan.luka.myseries.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
@Controller
@RequestMapping("/")
public class UserController {
    private final DataProvider model = DataProvider.getInstance();


    @GetMapping("/user")
    @ResponseBody
    public ArrayList<User> getUsers() {
        ArrayList<User> users = model.getUsers();
        return users;
    }


    private boolean isLoggedIn(HttpSession session) {
        return (session.getAttribute("username") != null);
    }
}
