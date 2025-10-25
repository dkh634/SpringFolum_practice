package jp.dkh634.springforum.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostForm {
    
	@Size(max = 20, message = "{postForm.authorName.size}")
    private String authorName;
	
	 @NotBlank(message = "{postForm.content.notNull}")
	 @Size(min = 1, max = 200, message = "{postForm.content.size}")
    private String content;
}
