package com.darc.shortlink.controller;

import com.darc.shortlink.domain.entities.ShortLink;
import com.darc.shortlink.domain.models.ShortLinkDto;
import com.darc.shortlink.services.ShortLinkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ShortLinkService shortLinkService;

    public HomeController(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @GetMapping("/")
    public String home(Model model) {
//        List<ShortUrl> shortLinks = shortLinkRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ShortLinkDto> shortLinks = shortLinkService.findPublicShortUrls();
        model.addAttribute("shortLinks", shortLinks);
        model.addAttribute("baseUrl", "http://localhost:8080/");
        return "index";
    }

}
