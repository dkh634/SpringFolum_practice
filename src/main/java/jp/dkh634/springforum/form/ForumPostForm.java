package jp.dkh634.springforum.form;

import lombok.Data;

@Data
public class ForumPostForm {
    
    private String authorName;
    private String content;
}
