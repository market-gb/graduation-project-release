package ru.nhp.user.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nhp.user.dto.JwtRequest;
import ru.nhp.user.services.UserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Методы для управления данными пользователя")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Метод для изменения email пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @PutMapping("/email")
    public void changeEmail(@RequestHeader @Parameter(description = "Имя пользователя", required = true) String username, @RequestBody @Parameter(description = "Данные пользователя с новым email", required = true) JwtRequest request){
        String email = request.getEmail();
        userService.changeEmail(username, email);
    }

    @Operation(
            summary = "Метод для изменения пароля пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @PutMapping("/password")
    public void changeAddress(@RequestHeader String username, @RequestBody @Parameter(description = "Данные пользователя с новым паролем", required = true) JwtRequest request){
        String password = request.getPassword();
        userService.changePassword(username, password);
    }
}
