package jfdi.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class DateTimeParser {
    private static DateTimeParser dateTimeParser;

    public static DateTimeParser getInstance() {
        return (dateTimeParser == null) ? dateTimeParser = new DateTimeParser()
                : dateTimeParser;
    }

    public List<LocalDateTime> parseDateTime(String input) {
        Parser parser = new Parser();
        List<DateGroup> dateTimeList = parser.parse(input);
        List<LocalDateTime> ldtList = new ArrayList<LocalDateTime>();
        for (DateGroup g : dateTimeList) {
            List<Date> dates = g.getDates();
            dates.forEach((date) -> ldtList.add(getLocalDateTimeFromDate(date)));
        }

        return ldtList;
    }

    public LocalDateTime getLocalDateTimeFromDate(Date d) {
        return LocalDateTime.ofInstant(d.toInstant(), Constants.ZONE_ID);
    }

    public static void main(String[] args) {
        DateTimeParser parser = DateTimeParser.getInstance();
        parser.parseDateTime("friday\nsaturday");

    }
}
