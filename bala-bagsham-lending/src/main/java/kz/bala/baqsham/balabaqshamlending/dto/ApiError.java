package kz.bala.baqsham.balabaqshamlending.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
    private int    status;
    private String error;
    private String message;
    private String path;
}
