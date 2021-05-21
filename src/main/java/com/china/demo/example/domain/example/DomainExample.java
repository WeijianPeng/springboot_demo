package com.china.demo.example.domain.example;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DomainExample {
    private String id;

    @NotBlank(message = "{example.name.not_empty}")
    private String name;

    private String description;

    private String createdBy;

    private String updatedBy;
}
