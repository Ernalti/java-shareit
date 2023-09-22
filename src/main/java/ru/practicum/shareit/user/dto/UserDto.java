package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {
	private Integer id;
	@NotBlank(groups = ValidationGroups.CreateUser.class)
	private String name;
	@NotBlank(groups = ValidationGroups.CreateUser.class)
	@Email(groups = {ValidationGroups.CreateUser.class, ValidationGroups.UpdateUser.class})
	private String email;
}
