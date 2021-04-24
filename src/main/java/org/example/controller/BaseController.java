package org.example.controller;

import org.example.utils.ResultMap;
import org.example.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        ResultMap.setInstance(Status.ERROR.getStatus(), exception.getMessage(), exception);
    }
}
