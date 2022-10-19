package com.study.server.modules.base;


import com.study.server.core.helper.MyMediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base", produces = MyMediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BaseController {
}
