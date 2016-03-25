package jfdi.test.logic.events;

import jfdi.logic.events.RescheduleTaskFailedEvent;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * @author Xinan
 */
public class RescheduleTaskFailedEventTest {

    @Mock
    private LocalDateTime startDateTime;

    @Mock
    private LocalDateTime endDateTime;

    private int screenId = 666;
    private RescheduleTaskFailedEvent.Error error = RescheduleTaskFailedEvent.Error.NON_EXISTENT_ID;

    @Test
    public void getScreenId() throws Exception {
        RescheduleTaskFailedEvent event = new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime, error);
        assertEquals(screenId, event.getScreenId());
    }

    @Test
    public void getStartDateTime() throws Exception {
        RescheduleTaskFailedEvent event = new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime, error);
        assertEquals(startDateTime, event.getStartDateTime());
    }

    @Test
    public void getEndDateTime() throws Exception {
        RescheduleTaskFailedEvent event = new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime, error);
        assertEquals(endDateTime, event.getEndDateTime());
    }

    @Test
    public void getError() throws Exception {
        RescheduleTaskFailedEvent event = new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime, error);
        assertEquals(error, event.getError());
    }

}
