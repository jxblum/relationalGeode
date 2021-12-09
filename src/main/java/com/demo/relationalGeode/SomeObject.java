package com.demo.relationalGeode;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.gemfire.util.ArrayUtils;
import org.springframework.lang.NonNull;

import lombok.Data;

@Data
@Region(name="some-object-region")
public class SomeObject {

    @Id
    private String someId;

    private List<CustomFirstObject> customFirstObjects;

    public @NonNull SomeObject identifiedAs(String someId) {
        setSomeId(someId);
        return this;
    }

    public @NonNull SomeObject withCustomFirstObjects(CustomFirstObject... objects) {
        setCustomFirstObjects(Arrays.asList(ArrayUtils.nullSafeArray(objects, CustomFirstObject.class)));
        return this;
    }
}
