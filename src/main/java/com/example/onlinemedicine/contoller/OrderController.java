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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('USER')")
    public List<OrderBucketResponseDto> save(@RequestBody OrderBucketRequestDto orderBucketRequestDto){
        return orderService.save(orderBucketRequestDto);
    }

    @GetMapping("/get-all")
    public List<OrderBucketResponseDto> getALl(){
        return orderService.getAll();
    }
    @GetMapping("/get-all-by-id")
    public List<OrderBucketResponseDto> getAllById(Principal principal){
        UUID userId = UUID.fromString(principal.getName());
        return orderService.getAllById(userId);
    }
    @DeleteMapping
    public OrderBucketResponseDto delete(@RequestParam UUID id){
       return orderService.delete(id);
    }
}
