package com.demo.relationalGeode;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Region(name="some-object-region")
public class SomeObject {
    @Id
    private String someId;
    private CustomFirstObject customFirstObject;
}
