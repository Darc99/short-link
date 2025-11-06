package com.darc.shortlink.controller;

import com.darc.shortlink.domain.models.CreateShortLinkForm;
import com.darc.shortlink.domain.models.ShortLinkDto;
import com.darc.shortlink.services.ShortLinkService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<ShortLinkDto> shortLinks = shortLinkService.findAllPublicShortUrls();
        model.addAttribute("shortLinks", shortLinks);
        model.addAttribute("baseUrl", "http://localhost:8080/");
        model.addAttribute("createShortLinkForm", new CreateShortLinkForm(""));
        return "index";
    }

    @PostMapping("/short-urls")
    String createShortUrl(@ModelAttribute("createShortLinkForm") @Valid CreateShortLinkForm form,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            List<ShortLinkDto> shortLinks = shortLinkService.findAllPublicShortUrls();
            model.addAttribute("shortLinks", shortLinks);
            model.addAttribute("baseUrl", "http://localhost:8080/");
            return "index";
        }



        redirectAttributes.addFlashAttribute("successMessage", "Short Url created successfully");
        return "redirect:/";
    }


}
