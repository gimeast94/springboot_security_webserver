package com.gimeast.securitywebserver.controller;

import com.gimeast.securitywebserver.dto.LoginForm;
import com.gimeast.securitywebserver.dto.LoginResultDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @GetMapping
    public String loginGet(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping
    public String loginPost(HttpSession httpSession, @RequestParam String id, @RequestParam String password) {

        String url = "http://localhost:8080";

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
                .build();

        ResponseEntity<LoginResultDto> result = webClient.post().uri(uriBuilder -> uriBuilder.path("/sign-api/sign-in")
                        .queryParam("id", id)
                        .queryParam("password", password)
                        .build())
                .retrieve().toEntity(LoginResultDto.class).block();

        System.out.println("code : " + result.getStatusCode());
        System.out.println("code value: " + result.getStatusCodeValue());
        System.out.println("header: " + result.getHeaders());
        System.out.println("body: " + result.getBody());
        System.out.println("token: " + result.getBody().getToken());

        httpSession.setAttribute("token", result.getBody().getToken());

        return "redirect:/";
    }

}
