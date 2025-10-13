package jp.dkh634.springforum.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "post")
public class Post {

	@Id
    private Long contentId;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
    private boolean deleted;
    private Long threadId;
}
