package com.example.onlinemedicine.contoller;


import com.example.onlinemedicine.dto.order.request.OrderBucketRequestDto;
import com.example.onlinemedicine.dto.order.response.OrderBucketResponseDto;
import com.example.onlinemedicine.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<OrderBucketResponseDto> save(@RequestBody OrderBucketRequestDto orderBucketRequestDto,Principal principal){
        UUID userId = UUID.fromString(principal.getName());
        return orderService.save(orderBucketRequestDto,userId);
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
//    @GetMapping("/get-by-id")
//    public OrderBucketResponseDto getById(Principal principal){
//        UUID userId = UUID.fromString(principal.getName());
//        orderService.getById(userId);
//    }
    @DeleteMapping
    public OrderBucketResponseDto delete(@RequestParam UUID id){
       return orderService.delete(id);
    }
}
