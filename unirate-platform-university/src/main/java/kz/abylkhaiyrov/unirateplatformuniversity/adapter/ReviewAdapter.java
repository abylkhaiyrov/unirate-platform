package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.ReviewDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewAdapter {

    ReviewDto toReviewDto(Review review);
}
