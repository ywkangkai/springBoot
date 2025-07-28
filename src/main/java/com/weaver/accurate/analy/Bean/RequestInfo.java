package com.weaver.accurate.analy.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfo {
    private String requestUrl;
    private RequestMethod mappingMethod;
}
