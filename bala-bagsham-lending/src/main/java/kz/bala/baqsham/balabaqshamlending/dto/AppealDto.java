package kz.bala.baqsham.balabaqshamlending.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppealDto {

    @NotBlank(message = "Поле ФИО обязательно")
    private String fio;

    @Pattern(
            regexp = "^(?:\\+7|8)7\\d{9}$",
            message = "Телефон должен быть в формате +77777777777 или 87777777777"
    )
    private String phoneNumber;

    @Size(max = 1_000)
    private String comment;
}
