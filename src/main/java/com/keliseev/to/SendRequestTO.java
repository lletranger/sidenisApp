package com.keliseev.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendRequestTO {

    @NotEmpty
    long from;

    @NotEmpty
    long to;

    @NotEmpty
    @Min(1)
    int amount;
}
