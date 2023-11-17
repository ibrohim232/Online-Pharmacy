package com.example.onlinemedicine.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotExistPharmacyResponse {

    private int status;
    private String description;
    private String error;
}
