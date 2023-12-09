package com.example.onlinemedicine.contoller;

import com.example.onlinemedicine.dto.payment.PaymentDto;
import com.example.onlinemedicine.dto.payment.PaymentResponseDto;
import com.example.onlinemedicine.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public void save(@RequestBody PaymentDto paymentDto){
        paymentService.save(paymentDto);
    }
    @GetMapping("/user-payments/{ownerId}")
    public List<PaymentResponseDto> getUserPayments(@PathVariable UUID ownerId){
        return paymentService.getUserPayments(ownerId);
    }
    @GetMapping("/find/{id}")
    public PaymentResponseDto findById(@PathVariable UUID id){
        return paymentService.findById(id);
    }
    @GetMapping("/get-all")
    public List<PaymentResponseDto> getAll(@RequestParam(defaultValue = "0",required = false) Integer page,
                                           @RequestParam(defaultValue = "10",required = false) Integer size){
        return paymentService.getAll(page,size);
    }
}
