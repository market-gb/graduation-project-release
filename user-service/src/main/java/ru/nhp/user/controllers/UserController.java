package ru.nhp.user.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.api.exceptions.AppError;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.user.dto.JwtRequest;
import ru.nhp.user.entites.User;
import ru.nhp.user.services.RoleService;
import ru.nhp.user.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Методы для управления данными пользователя")
public class UserController {
    private final UserService userService;

    private final RoleService roleService;

    @Operation(
            summary = "Запрос на получение всех имеющихся в БД пользователей",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "4XX",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping
    public Page<User> searchUsers(@RequestParam(name = "p", defaultValue = "1") Integer page,
        @RequestParam(name = "page_size", defaultValue = "9") Integer pageSize) {
            if (page < 1) {
                page = 1;
            }
            return userService.searchUsers(page, pageSize);
    }

    @Operation(
            summary = "Запрос на получение пользователя по идентификатору",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public User getById(
            @PathVariable @Parameter(description = "Идентификатор пользователя", required = true) Long id) {
        return userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден, идентификатор: " + id));
    }

    @Operation(
            summary = "Запрос на получение имен всех имеющихся в БД ролей",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = List.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "4XX",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping("/roles")
    public List<String> getAllRoleNames() {
        return roleService.findAllRoleNames();
    }

    @Operation(
            summary = "Изменение роли пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "4XX",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PatchMapping("/roles/{id}")
    public void changeRole(@Parameter(description = "Имя роли", required = true) @RequestParam String roleName,
                           @Parameter(description = "Идентификатор пользователя", required = true) @PathVariable Long id) {
        userService.changeRole(roleName, id);
    }

    @Operation(
            summary = "Метод для изменения email пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "4XX",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PatchMapping("/email/{id}")
    public void changeEmail(@RequestBody @Parameter(description = "Данные пользователя с новым email", required = true) JwtRequest request,
                            @Parameter(description = "Идентификатор пользователя", required = true) @PathVariable Long id){
        String email = request.getEmail();
        userService.changeEmail(id, email);
    }

    @Operation(
            summary = "Метод для изменения пароля пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "4XX",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PatchMapping("/password/{id}")
    public void changePassword(@RequestBody @Parameter(description = "Данные пользователя с новым паролем", required = true) JwtRequest request,
                               @Parameter(description = "Идентификатор пользователя", required = true) @PathVariable Long id){
        String password = request.getPassword();
        userService.changePassword(id, password);
    }

    @Operation(
            summary = "Удаление пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Parameter(description = "Идентификатор пользователя", required = true) Long id) {
        userService.deleteById(id);
    }
}
