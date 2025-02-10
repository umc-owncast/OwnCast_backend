package com.umc.owncast.common.config;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() == 401) {
                return new UserHandler(ErrorCode.NOT_LOGGED_IN);
            } else if (response.status() == 404) {
                return new UserHandler(ErrorCode.MEMBER_NOT_FOUND);
            }
            return FeignException.errorStatus(methodKey, response);
        }
    }
}
