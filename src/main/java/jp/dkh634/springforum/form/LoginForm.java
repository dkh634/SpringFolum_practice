package jp.dkh634.springforum.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {

	@NotBlank(message = "{loginForm.content.notNull}")
    private String username;
	 
	@NotBlank(message = "{loginForm.content.notNull}")
    private String password;

  
}
