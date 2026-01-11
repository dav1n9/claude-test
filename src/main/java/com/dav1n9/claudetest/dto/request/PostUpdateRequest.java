package com.dav1n9.claudetest.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {

    @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
    private String title;

    private String content;
}