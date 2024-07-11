package com.umc.owncast.common.exception.handler;

import com.umc.owncast.common.exception.GeneralException;
import com.umc.owncast.common.response.BaseErrorCode;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode code) {
        super(code);
    }
}
