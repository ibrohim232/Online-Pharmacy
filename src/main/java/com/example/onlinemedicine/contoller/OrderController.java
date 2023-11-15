package com.example.onlinemedicine.contoller;


import com.example.onlinemedicine.dto.order.request.OrderBucketRequestDto;
import com.example.onlinemedicine.dto.order.response.OrderBucketResponseDto;
import com.example.onlinemedicine.service.OrderService;
;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    @Operation(
            description = "This API is used for creating orders in the database",
            method = "Post method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"USER"})
    )
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('USER')")
    public OrderBucketResponseDto save(@RequestBody OrderBucketRequestDto orderBucketRequestDto){
        return orderService.save(orderBucketRequestDto);
    }
    @Operation(
            description = "This API is used for getting all orders in the database",
            method = "GET method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"USER"})
    )
    @GetMapping("/get-all")
    public List<OrderBucketResponseDto> getALl(){
        return orderService.getAll();
    }
    @Operation(
            description = "This API is used for getting all orders by user id",
            method = "GET method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"USER"})
    )
    @GetMapping("/get-all-by-id")
    public List<OrderBucketResponseDto> getAllById(@RequestParam UUID id){
        List<OrderBucketResponseDto> orderBucketResponseDtos = orderService.getAllById(id);
        return orderBucketResponseDtos;
    }
    @Operation(
            description = "This API is used for deleting order by id",
            method = "GET method is supported",
            security = @SecurityRequirement(name = "pre authorize", scopes = {"USER"})
    )
    @DeleteMapping
    public OrderBucketResponseDto delete(@RequestParam UUID id){
       return orderService.delete(id);
    }
}
