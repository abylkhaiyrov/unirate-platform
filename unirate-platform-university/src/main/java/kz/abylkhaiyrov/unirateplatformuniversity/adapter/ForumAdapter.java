package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.ForumDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.ReviewReturnDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Forum;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ForumAdapter {

    @Mapping(target = "universityId", source = "university.id")
    @Mapping(target = "universityImgUrl", source = "university.logoUrl")
    @Mapping(target = "topReviews", expression = "java(getTopReviews(forum.getReviews()))")
    ForumDto entity2Dto(Forum forum);

    default List<ReviewReturnDto> getTopReviews(List<Review> reviews) {
        if (reviews == null) {
            return new ArrayList<>();
        }
        return reviews.stream()
                .sorted(Comparator.comparing(Review::getLikes).reversed())
                .limit(3)
                .map(review -> {
                    ReviewReturnDto dto = new ReviewReturnDto();
                    dto.setId(review.getId());
                    dto.setForumId(review.getForum().getId());
                    dto.setComment(review.getComment());
                    dto.setRating(review.getRating());
                    dto.setUserId(review.getUserId());
                    dto.setStatus(review.getStatus().name());
                    dto.setLikes(review.getLikes());
                    dto.setDislikes(review.getDislikes());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
