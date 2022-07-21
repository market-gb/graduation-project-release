package ru.nhp.core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.nhp.api.dto.core.enums.OrderStatus;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.core.converters.OrderConverter;
import ru.nhp.api.dto.core.OrderDetailsDto;
import ru.nhp.api.dto.core.OrderDto;
import ru.nhp.core.exceptions.CoreValidationException;
import ru.nhp.core.exceptions.CoreAppError;
import ru.nhp.core.services.OrderService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Заказы", description = "Методы работы с заказами")
public class OrdersController {
    private final OrderService ordersService;
    private final OrderConverter orderConverter;

    @Operation(
            summary = "Создание заказа",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = OrderDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "4XX",
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto save(@RequestHeader @Parameter(description = "Имя пользователя", required = true) String username, @RequestBody @Valid @Parameter(description = "Детали заказа", required = true) OrderDetailsDto orderDetailsDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new CoreValidationException("Ошибка валидации", errors);
        }
        return orderConverter.entityToDto(ordersService.save(username, orderDetailsDto));
    }

    @Operation(
            summary = "Заказы текущего пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = List.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "4XX",
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
                    )
            }
    )
    @GetMapping
    public List<OrderDto> getAllByUsername(@RequestHeader @Parameter(description = "Имя пользователя", required = true) String username) {
        return ordersService.findAllByUsername(username).stream()
                .map(orderConverter::entityToDto).toList();
    }

    @Operation(
            summary = "Запрос на получение заказа по идентификатору",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OrderDto.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable @Parameter(description = "Идентификатор заказа", required = true) Long id) {
        return orderConverter.entityToDto(ordersService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Заказ не найден, идентификатор: " + id)));
    }

    @Operation(
            summary = "Изменение статуса заказа",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "4XX",
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
                    )
            }
    )
    @PatchMapping("/{id}")
    public void changeStatus(@Parameter(description = "Статус заказа", required = true) @RequestBody OrderStatus orderStatus,
                             @Parameter(description = "Идентификатор заказа", required = true) @PathVariable Long id) {
        ordersService.changeStatus(orderStatus, id);
    }

    @Operation(
            summary = "Удаление заказа",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Parameter(description = "Идентификатор заказа", required = true) Long id) {
        ordersService.deleteById(id);
    }
}
