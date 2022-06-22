package dev.road.map.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	
    @RequestMapping("/.well-known/acme-challenge/LTcxel4Uz0HlhDTfRQ3J2XU0tBLRvK3Qu1ShR1VRXec")
    public String test() {
    	return "LTcxel4Uz0HlhDTfRQ3J2XU0tBLRvK3Qu1ShR1VRXec.Y5ALzqwzZDW7sD_S2KrEcqQxK4qPIeNk7BXMYq7-e_U";
    }
}
