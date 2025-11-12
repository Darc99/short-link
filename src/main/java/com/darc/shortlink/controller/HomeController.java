package com.darc.shortlink.controller;

import com.darc.shortlink.ApplicationProperties;
import com.darc.shortlink.domain.entities.User;
import com.darc.shortlink.domain.exceptions.ShortLinkNotFoundException;
import com.darc.shortlink.domain.models.CreateShortLinkForm;
import com.darc.shortlink.domain.models.CreateShortUrlCmd;
import com.darc.shortlink.domain.models.ShortLinkDto;
import com.darc.shortlink.services.ShortLinkService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ShortLinkService shortLinkService;
    private final ApplicationProperties properties;
    private final SecurityUtils securityUtils;

    public HomeController(ShortLinkService shortLinkService, ApplicationProperties properties, SecurityUtils securityUtils) {
        this.shortLinkService = shortLinkService;
        this.properties = properties;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/")
    public String home(Model model) {
//        User currentUser = securityUtils.getCurrentUser();

//        List<ShortUrl> shortLinks = shortLinkRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ShortLinkDto> shortLinks = shortLinkService.findAllPublicShortUrls();
        model.addAttribute("shortLinks", shortLinks);
        model.addAttribute("baseUrl", properties.baseUrl());
        model.addAttribute("createShortLinkForm", new CreateShortLinkForm("", false, null));
        return "index";
    }

    @PostMapping("/short-urls")
    String createShortUrl(@ModelAttribute("createShortLinkForm") @Valid CreateShortLinkForm form,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            List<ShortLinkDto> shortLinks = shortLinkService.findAllPublicShortUrls();
            model.addAttribute("shortLinks", shortLinks);
            model.addAttribute("baseUrl", properties.baseUrl());
            return "index";
        }

        try {
            Long userId = securityUtils.getCurrentUserId();
            CreateShortUrlCmd cmd = new CreateShortUrlCmd(
                    form.originalUrl(),
                    form.isPrivate(),
                    form.expirationInDays(),
                    userId
                    );
            var shortUrlDto = shortLinkService.createShortUrl(cmd);
            redirectAttributes.addFlashAttribute("successMessage", "Short Url created successfully" +
                    properties.baseUrl()+"/s/"+shortUrlDto.shortKey());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create short url");
        }
        return "redirect:/";
    }

    @GetMapping("/s/{shortKey}")
    String redirectToOriginalUrl(@PathVariable String shortKey) {
        Long userId = securityUtils.getCurrentUserId();
        Optional<ShortLinkDto> shortLinkDtoOptional = shortLinkService.accessShortUrl(shortKey, userId);
        if (shortLinkDtoOptional.isEmpty()) {
            throw new ShortLinkNotFoundException("Short link does not exist: " + shortKey);
        }
        ShortLinkDto shortLinkDto = shortLinkDtoOptional.get();
        return "redirect:"+shortLinkDto.originalUrl();
    }

    @GetMapping("/login")
    String loginForm() {
        return "login";
    }

}
