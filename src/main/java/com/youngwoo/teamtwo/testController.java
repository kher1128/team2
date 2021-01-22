package com.youngwoo.teamtwo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class testController {

    @GetMapping("/index.html")
    public String Index(){
        return "index";
    }
}
