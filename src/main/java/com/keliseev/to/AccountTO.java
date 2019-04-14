package com.keliseev.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTO {

    @NotBlank
    private String name;

    @NotEmpty
    @Min(0)
    private int amount;
}
