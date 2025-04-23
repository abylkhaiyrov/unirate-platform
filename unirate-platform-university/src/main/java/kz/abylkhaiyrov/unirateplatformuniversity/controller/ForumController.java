package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateForumDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.ForumDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.ForumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/forums")
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    /**
     * Создание нового форума.
     *
     * @param createForumDto DTO с данными для создания форума
     * @return DTO созданного форума
     */
    @PostMapping
    public ForumDto createForum(@RequestBody CreateForumDto createForumDto) {
        return forumService.createForum(createForumDto);
    }

    /**
     * Получение форума по идентификатору.
     *
     * @param id идентификатор форума
     * @return DTO форума
     */
    @GetMapping("/{id}")
    public ForumDto getForumById(@PathVariable Long id) {
        return forumService.getForumById(id);
    }

    /**
     * Получение списка всех форумов по университету.
     *
     * @return список DTO форумов
     */
    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<ForumDto>> getForumsWithUniversity(@PathVariable Long universityId) {
        return ResponseEntity.ok(forumService.getForumsWithUniversity(universityId));
    }

    /**
     * Получение списка всех форумов.
     *
     * @return список DTO форумов
     */
    @GetMapping
    public List<ForumDto> getAllForums() {
        return forumService.getAllForums();
    }

    /**
     * Поиск форумов по имени (частичное совпадение без учета регистра).
     *
     * @param name часть или полное название форума
     * @return список DTO форумов, удовлетворяющих условию поиска
     */
    @GetMapping("/search")
    public List<ForumDto> searchForumsByName(@RequestParam String universityName,@RequestParam String name) {
        return forumService.searchForumsByName(universityName, name);
    }

    /**
     * Получение страницы форумов с использованием пагинации.
     *
     * @param pageable объект с параметрами пагинации (page, size, sort)
     * @return страница DTO форумов
     */
    @GetMapping("/page")
    public Page<ForumDto> getForumsPage(Pageable pageable) {
        return forumService.getForumsPage(pageable);
    }

    @PutMapping("/{forumId}/update")
    public ResponseEntity<String> updateProfileUrl(
            @PathVariable("forumId") Long forumId,
            @RequestParam("url") String url) {
        return ResponseEntity.ok(forumService.updateForumPicture(forumId, url));
    }
}
