package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.ReviewDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewAdapter {

    @Mapping(target = "id" , source = "id")
    @Mapping(target = "forumId", source = "forum.id")
    ReviewDto toReviewDto(Review review);
}
