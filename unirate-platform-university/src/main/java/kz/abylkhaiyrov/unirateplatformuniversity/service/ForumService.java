package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.ForumAdapter;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateForumDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.ForumDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Forum;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.ForumRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;
    private final UniversityRepository universityRepository;
    private final ForumAdapter forumAdapter;

    /**
     * Создает новый форум.
     *
     * @param createForumDto DTO с данными для создания форума
     * @return ForumDto с данными созданного форума
     */
    public ForumDto createForum(CreateForumDto createForumDto) {
        log.info("Creating Forum: {}", createForumDto);
        var forum = new Forum();
        forum.setName(createForumDto.getName());
        forum.setDescription(createForumDto.getDescription());

        var university = universityRepository.findById(createForumDto.getUniversityId())
                .orElseThrow(() -> new NotFoundException("University not found with id: " + createForumDto.getUniversityId()));
        forum.setUniversity(university);

        forum = forumRepository.save(forum);
        log.info("Forum created with id: {}", forum.getId());
        return forumAdapter.entity2Dto(forum);
    }

    /**
     * Получает форум по идентификатору.
     *
     * @param id идентификатор форума
     * @return ForumDto с данными форума
     */
    @Transactional(readOnly = true)
    public ForumDto getForumById(Long id) {
        log.info("Fetching Forum by id: {}", id);
        var forum = forumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Forum not found with id: " + id));
        ForumDto forumDto = forumAdapter.entity2Dto(forum);
        log.info("Fetched Forum: {}", forumDto);
        return forumDto;
    }

    /**
     * Получает список всех форумов по университету.
     *
     * @return список ForumDto
     */
    @Transactional(readOnly = true)
    public List<ForumDto> getForumsWithUniversity(Long universityId) {
        log.info("Fetching Forums with university id: {}", universityId);
        var university = universityRepository.findById(universityId).orElseThrow(() -> new NotFoundException("University not found with id: " + universityId));
        return forumRepository.findAllByUniversity(university)
                .stream()
                .map(forumAdapter::entity2Dto)
                .collect(Collectors.toList());
    }

    /**
     * Получает список всех форумов.
     *
     * @return список ForumDto
     */
    @Transactional(readOnly = true)
    public List<ForumDto> getAllForums() {
        log.info("Fetching all Forums");
        var forumDtos = forumRepository.findAll()
                .stream()
                .map(forumAdapter::entity2Dto)
                .collect(Collectors.toList());
        log.info("Fetched {} forums", forumDtos.size());
        return forumDtos;
    }

    /**
     * Ищет форумы по наименованию с частичным совпадением (без учета регистра).
     *
     * @param name часть или полное название форума
     * @return список ForumDto с найденными форумами
     */
    @Transactional(readOnly = true)
    public List<ForumDto> searchForumsByName(String name) {
        log.info("Searching forums by name matching: {}", name);
        // Выполняем поиск форумов, где имя форума частично совпадает с переданным параметром (без учета регистра)
        var forumDtos = forumRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(forumAdapter::entity2Dto)
                .collect(Collectors.toList());
        log.info("Found {} forums matching name '{}'", forumDtos.size(), name);
        return forumDtos;
    }

    /**
     * Возвращает страницы форумов с использованием пагинации.
     *
     * @param pageable объект с параметрами пагинации
     * @return страницу ForumDto
     */
    @Transactional(readOnly = true)
    public Page<ForumDto> getForumsPage(Pageable pageable) {
        log.info("Fetching forum page: {}", pageable);
        Page<Forum> page = forumRepository.findAll(pageable);
        Page<ForumDto> dtoPage = page.map(forumAdapter::entity2Dto);
        log.info("Fetched page {} with {} forums", pageable.getPageNumber(), dtoPage.getNumberOfElements());
        return dtoPage;
    }

    public String updateForumPicture(Long forumId, String url) {
        var user = forumRepository.findById(forumId)
                .orElseThrow(() -> new IllegalArgumentException("Forum doesn't exist with id: " + forumId));
        try {
            if (url != null && (user.getForumPicture() == null || !user.getForumPicture().equals(url))) {
                user.setForumPicture(url);
            }
            forumRepository.save(user);
            return "Forum updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Forum update failed";
        }
    }

    public void deleteForum(Long forumId) {
        log.info("Deleting Forum with id: {}", forumId);
        var forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new IllegalArgumentException("Forum doesn't exist with id: " + forumId));
        forumRepository.delete(forum);
    }
}
