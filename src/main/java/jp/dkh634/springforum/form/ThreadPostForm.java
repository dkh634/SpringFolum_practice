package jp.dkh634.springforum.form;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ThreadPostForm {
	
//	@NotBlank(message = "{threadPostForm.title.notNull}")
	@Size(min = 1, max = 20, message = "{threadPostForm.title.size}")
	private String title;
}
