package jp.dkh634.springforum.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForumPostForm {
    
	@Size(max = 20, message = "{forumPostForm.authorName.size}")
    private String authorName;
	
	 @NotBlank(message = "{forumPostForm.content.notNull}")
	 @Size(min = 1, max = 200, message = "{forumPostForm.content.size}")
    private String content;
}
