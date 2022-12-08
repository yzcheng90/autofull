package com.suke.zhjg.common.autofull.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suke.zhjg.common.autofull.decode.DecodeMaskDataHandle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author czx
 * @title: CustomHttpInputMessage
 * @projectName zhjg
 * @description: TODO 参数解析
 * @date 2021/1/2814:09
 */
public class CustomHttpInputMessage implements HttpInputMessage {

    private HttpHeaders headers;
    private InputStream body;

    public CustomHttpInputMessage(HttpInputMessage httpInputMessage, ObjectMapper objectMapper,Type type) throws IOException {
        this.headers = httpInputMessage.getHeaders();
        this.body = DecodeMaskDataHandle.decode(httpInputMessage.getBody(),objectMapper,type);
    }

    @Override
    public InputStream getBody() throws IOException {
        return this.body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.headers;
    }
}
