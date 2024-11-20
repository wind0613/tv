package io.github.bigmouthcn.m3u8checker.parser;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Configuration
public class M3uParserFactory implements ApplicationContextAware {

    private final Map<M3uSource, M3uParser> parsers = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(M3uParser.class).values().forEach(this::register);
    }

    private void register(M3uParser parser) {
        parsers.put(parser.getSource(), parser);
    }

    public M3uParser getParser(M3uSource source) {
        return parsers.get(source);
    }
}
