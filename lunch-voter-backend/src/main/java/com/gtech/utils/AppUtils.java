package com.gtech.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {
    private static MessageSource messageSource;
    private static String appVersion;

    @Autowired
    private AppUtils(MessageSource messageSource,
                     @Value("${application.version:1.x}") String appVersion) {
        AppUtils.messageSource      = messageSource;
        AppUtils.appVersion         = appVersion;
    }

    public static String getMessage(String messageKey, Object... args) {
        return AppUtils.messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }

    public static String getAppVersion() {
        return appVersion;
    }
}
