package service;

import util.ApplicationConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Shop {

    private List<DayOfWeek> openingDays = new ArrayList<>();
    private LocalTime openingHoursStart;
    private LocalTime openingHoursEnd;

    public Shop() {
        loadProperties();
    }

    private void loadProperties() {
        Properties prop = new Properties();
        try {
            //load a properties file from resources folder
            prop.load(new FileInputStream(ApplicationConstants.ShopConstant.PROPERTIES_PATH));

            //get the property value
            String[] days = prop.getProperty(ApplicationConstants.ShopConstant.OPENING_HOURS).split(",");
            openingDays = Arrays.stream(days).map(day -> DayOfWeek.valueOf(day)).collect(Collectors.toList());

            String[] openingHoursStartString = prop.getProperty(ApplicationConstants.ShopConstant.OPENING_HOURS_START).split(":");
            openingHoursStart = LocalTime.of(Integer.valueOf(openingHoursStartString[0]), Integer.valueOf(openingHoursStartString[1]));

            String[] openingHoursEndString = prop.getProperty(ApplicationConstants.ShopConstant.OPENING_HOURS_END).split(":");
            openingHoursEnd = LocalTime.of(Integer.valueOf(openingHoursEndString[0]), Integer.valueOf(openingHoursEndString[1]));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ZonedDateTime nextOpeningDate(ZonedDateTime date) {
        if (isAnOpeningDay(date) && isUpToOpeningHour(date.toLocalTime())) {
            return ZonedDateTime.of(date.toLocalDate(), openingHoursStart, date.getZone());
        }
        ZonedDateTime nextDay = date.plusDays(1);
        return nextOpeningDate(ZonedDateTime.of(nextDay.toLocalDate(), openingHoursStart, nextDay.getZone()));
    }

    public boolean isOpenOn(ZonedDateTime date) {
        return isAnOpeningDay(date) && isAnOpeningHour(date.toLocalTime());
    }

    private boolean isAnOpeningDay(ZonedDateTime date) {
        return openingDays.contains(date.getDayOfWeek());
    }

    private boolean isAnOpeningHour(LocalTime hour) {
        return !hour.isBefore(openingHoursStart) && !hour.isAfter(openingHoursEnd);
    }

    private boolean isUpToOpeningHour(LocalTime hour) {
        return !hour.isAfter(openingHoursStart);
    }

}