package jp.dkh634.springforum.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "thread")
public class ForumThread  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long threadId;
	private String title;
	private LocalDateTime createdAt;
	private boolean deleted;
}
