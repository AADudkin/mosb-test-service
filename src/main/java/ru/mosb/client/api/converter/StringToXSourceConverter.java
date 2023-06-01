package ru.mosb.client.api.converter;

import org.springframework.core.convert.converter.Converter;
import ru.mosb.client.dto.XSource;
import ru.mosb.client.exception.NoSuchSourceException;

public class StringToXSourceConverter implements Converter<String, XSource> {
    @Override
    public XSource convert(String source) {
        try {
            return XSource.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NoSuchSourceException(source);
        }
    }
}
