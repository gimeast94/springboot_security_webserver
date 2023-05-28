package com.gimeast.securitywebserver.controller;

import com.gimeast.securitywebserver.dto.LoginResultDto;
import com.gimeast.securitywebserver.dto.ProductDto;
import com.gimeast.securitywebserver.dto.ProductResultDto;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

    static final String url = "http://localhost:8080";


    @GetMapping(value = "/{number}")
    public String productGetDetail(HttpSession httpSession, Model model, @PathVariable String number) {

        String token = httpSession.getAttribute("token").toString();
        System.out.println("productGet session[token] value : " + token);

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        ProductResultDto result = webClient.get().uri(uriBuilder -> uriBuilder.path("/product")
                        .queryParam("number", number)
                        .build())
                        .header("X-AUTH-TOKEN", token)
                        .exchangeToMono(clientResponse -> {
                            if(clientResponse.statusCode().equals(HttpStatus.OK)) {
                                return clientResponse.bodyToMono(ProductResultDto.class);
                            }else {
                                return clientResponse.createException().flatMap(Mono::error);
                            }
                        })
                        .block();

        System.out.println("result : " + result);
        model.addAttribute("product", result);

        return "product";
    }

    @GetMapping(value = "/createForm")
    public String productCreateForm() {
        return "createProduct";
    }

    @PostMapping(value = "/create")
    public String productPost(HttpSession httpSession, @RequestParam String name, @RequestParam int stock, @RequestParam int price) {
        String token = httpSession.getAttribute("token").toString();

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
                .build();

        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setStock(stock);
        productDto.setPrice(price);

        ResponseEntity<ProductResultDto> result = webClient.post().uri(uriBuilder -> uriBuilder.path("/product")
                .build())
                .bodyValue(productDto)
                .header("X-AUTH-TOKEN", token)
                .retrieve().toEntity(ProductResultDto.class).block();

        System.out.println("result : " + result);

        return "redirect:/";
    }



}
