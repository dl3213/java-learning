package code.sibyl.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


@Slf4j
public class I18nUtil {

    public static String message(String code, Object... args) {
        MessageSource messageSource = r.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
