package com.example.finalbackend.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class pageController {

    @GetMapping("/home")
    public static String homePage(Model model, HttpServletRequest request) throws IOException, JSONException {
        return "hello";
    }

}
