package jp.dkh634.springforum.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ThreadForm {
	
	@NotBlank(message = "{threadForm.title.notNull}")
	@Size(max = 20, message = "{threadForm.title.size}")
	private String title;
}