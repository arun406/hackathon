package com.accelya.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LOWrapper {
    private MultipartFile file;
    private String lo; // AirwayBillDTO
}
