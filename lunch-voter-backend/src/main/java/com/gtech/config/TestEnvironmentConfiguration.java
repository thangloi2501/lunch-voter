package com.gtech.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gtech.utils.AppUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class TestEnvironmentConfiguration {

  @Bean
  public ObjectWriter objectWriter() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

    return mapper.writer().withDefaultPrettyPrinter();
  }

  @Bean
  public MessageSource messageSource() {
    return new MessageSource() {
      @Override
      public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return code;
      }

      @Override
      public String getMessage(String code, Object[] args, Locale locale) {
        return code;
      }

      @Override
      public String getMessage(MessageSourceResolvable resolvable, Locale locale) {
        return String.join(",", resolvable.getCodes());
      }
    };
  }

  @Bean
  public AppUtils appUtils(MessageSource messageSource)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    Constructor<AppUtils> contructor = AppUtils.class.getDeclaredConstructor(MessageSource.class,
        String.class);
    ReflectionUtils.makeAccessible(contructor);
    return contructor.newInstance(messageSource, "1.0");
  }
}
