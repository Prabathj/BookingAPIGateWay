package com.prabathj.hotelavailabilitysrv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prabathj.bookinggw.utilities.GlobalExceptionHandler;

public class AvailabilityServerConfiguration {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    static ObjectMapper getObjectMapper() {
    	OBJECT_MAPPER.registerModule(new JavaTimeModule());
        return OBJECT_MAPPER;
    }

    public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}
