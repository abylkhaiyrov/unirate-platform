package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UniversityReviewStats {
    private double averageRating;
    private long reviewCount;
}