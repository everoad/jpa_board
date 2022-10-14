package com.study.server.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ApiResponse<T> {

    private T data;

    public ApiResponse(T data) {
        this.data = data;
    }

}
