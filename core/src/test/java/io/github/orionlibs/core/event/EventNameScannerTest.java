package io.github.orionlibs.core.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EventNameScannerTest
{
    @Autowired EventNameScanner eventNameScanner;


    @Test
    void scanEventNames()
    {
        List<String> allEventNames = eventNameScanner.scanEventNames("io.github.orionlibs.core.event");
        assertThat(List.of("event-1", "event-2", "event-3")).isEqualTo(allEventNames);
    }


    @Event
    public static class TestEvent1
    {
        public static final String EVENT_NAME = "event-1";
    }


    @Event
    public static class TestEvent2
    {
        public static final String EVENT_NAME = "event-2";
    }


    @Event
    public static class TestEvent3
    {
        public static final String EVENT_NAME = "event-3";
    }
}
