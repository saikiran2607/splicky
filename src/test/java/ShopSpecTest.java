import org.junit.jupiter.api.Test;
import service.Shop;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class ShopSpecTest {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private Shop shop;

    public ShopSpecTest() {
        this.shop = new Shop();
    }

    @Test
    public void testIsOpenOnOpeningDay() {
        ZonedDateTime wednesday = ZonedDateTime.parse("2016-05-11T12:22:11.824Z", dateTimeFormatter);
        assertTrue(shop.isOpenOn(wednesday));
    }

    @Test
    public void testIsClosedOnClosingHours() {
        ZonedDateTime wednesdayClosedHours = ZonedDateTime.parse("2016-05-11T17:22:11.824Z", dateTimeFormatter);
        assertFalse(shop.isOpenOn(wednesdayClosedHours));
    }

    @Test
    public void testIsClosedOnClosingDay() {
        ZonedDateTime thursday = ZonedDateTime.parse("2016-05-12T12:22:11.824Z", dateTimeFormatter);
        assertFalse(shop.isOpenOn(thursday));
    }

    @Test
    public void testIsClosedBeforeOpeningHour() {
        ZonedDateTime wednesdayBeforeOpeningHour = ZonedDateTime.parse("2016-05-11T07:22:11.824Z", dateTimeFormatter);
        assertFalse(shop.isOpenOn(wednesdayBeforeOpeningHour));
    }

    @Test
    public void testIsOpenAtOpeningHour() {
        ZonedDateTime wednesdayAtOpeningHour = ZonedDateTime.parse("2016-05-11T08:00:00Z", dateTimeFormatter);
        assertTrue(shop.isOpenOn(wednesdayAtOpeningHour));
    }

    @Test
    public void testNextOpeningDate() {
        ZonedDateTime wednesday = ZonedDateTime.parse("2016-05-11T12:22:11.824Z", dateTimeFormatter);
        ZonedDateTime fridayMorning = ZonedDateTime.parse("2016-05-13T08:00:00.000Z", dateTimeFormatter);
        assertEquals(fridayMorning, shop.nextOpeningDate(wednesday));
    }

    @Test
    public void testCurrentDayAsOpeningDayBeforeOpeningHour() {
        ZonedDateTime wednesdayBeforeOpeningDay = ZonedDateTime.parse("2016-05-11T06:22:11.824Z", dateTimeFormatter);
        ZonedDateTime wednesdayAtOpeningHour = ZonedDateTime.parse("2016-05-11T08:00:00Z", dateTimeFormatter);
        assertEquals(wednesdayAtOpeningHour, shop.nextOpeningDate(wednesdayBeforeOpeningDay));
    }
}
