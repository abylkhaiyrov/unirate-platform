package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;

@Getter
public class CreateForumDto {

    private String name;

    private String description;

    private Long universityId;
}
