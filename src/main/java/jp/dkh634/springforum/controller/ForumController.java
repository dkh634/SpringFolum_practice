package jp.dkh634.springforum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.dkh634.springforum.service.ForumService;

@RequestMapping("/api/forum")
@RestController 
public class ForumController {
	@Autowired
    private ForumService forumService;

}
