package jp.dkh634.springforum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.dkh634.springforum.service.ForumService;

@Controller
public class ForumController {
	@Autowired
    private ForumService forumService;

	@GetMapping("/api/home")
	public String displayHome() {
		return "home";
	}
}
