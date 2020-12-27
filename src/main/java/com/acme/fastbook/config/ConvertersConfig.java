package com.acme.fastbook.config;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

/**
 * Spring configuration related to application wide type conversions, including serialization/deserialization
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Configuration
public class ConvertersConfig {

	/**
	 * Jackson's {@link ObjectMapper} configuration related to date conversions.
	 * Spring will use default Jackson's {@link InstantDeserializer} to deserialize into 
	 * {@link ZonedDateTime}. We configure corresponding serializer to setup the same time format
	 * which is represented by DateTimeFormatter.ISO_INSTANT. The ISO instant formatter 
	 * formats or parses an instant in UTC,such as '2011-12-03T10:15:30.123Z' (zulu format). Milliseconds are optional
	 * and can vary from 0 to 9 digits. 
	 * 
	 * 
	 * @return customized bean {@link Jackson2ObjectMapperBuilderCustomizer}
	 */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
            builder.serializers(new ZonedDateTimeSerializer(DateTimeFormatter.ISO_INSTANT));
            // by default, Jackson uses InstantDeserializer to deserialize ZonedDateTime
        };
    }

}
