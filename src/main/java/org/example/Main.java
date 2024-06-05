package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dev.MedianFinder;
import org.example.models.Ticket;
import org.example.models.TicketList;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> minFlightTimeForCarrier = new HashMap<>();
        ArrayList<Integer> allPrice = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy;HH:mm");
        SimpleDateFormat resultFormatterToParse = new SimpleDateFormat("H:m");
        SimpleDateFormat resultFormatterToFormat = new SimpleDateFormat("HH:mm");
        TicketList ticketList = mapper.readValue(new File("src/main/resources/tickets.json"), TicketList.class);

        for (Ticket ticket : ticketList.getTickets()) {
            if (Objects.equals(ticket.getOrigin(), "VVO") && Objects.equals(ticket.getDestination(), "TLV")) {
                allPrice.add(ticket.getPrice());
                Date departureDate = formatter.parse(ticket.getDeparture_date() + ";" + ticket.getDeparture_time());
                Date arrivalDate = formatter.parse(ticket.getArrival_date() + ";" + ticket.getArrival_time());
                Duration duration = Duration.between(departureDate.toInstant(), arrivalDate.toInstant());
                if (!minFlightTimeForCarrier.containsKey(ticket.getCarrier())) {
                    minFlightTimeForCarrier.put(
                            ticket.getCarrier(),
                            resultFormatterToFormat.format(resultFormatterToParse.parse(duration.toHoursPart() + ":" + duration.toMinutesPart()))
                    );
                } else {
                    if (resultFormatterToFormat.parse(minFlightTimeForCarrier.get(ticket.getCarrier()))
                            .after(resultFormatterToParse.parse(duration.toHoursPart() + ":" + duration.toMinutesPart()))) {
                        minFlightTimeForCarrier.put(
                                ticket.getCarrier(),
                                resultFormatterToFormat.format(resultFormatterToParse.parse(duration.toHoursPart() + ":" + duration.toMinutesPart()))
                        );
                    }
                }
            }
        }

        Integer allPriceSum = 0;
        for (Integer price : allPrice) {
            allPriceSum += price;
        }
        Double averagePrice = allPriceSum.doubleValue() / allPrice.size();
        Double median = MedianFinder.findMedian(allPrice);

        System.out.println("Разница между средней ценной и медианой: " + (averagePrice - median));
        System.out.println("Минимальные время для перевозчиков: ");
        for (HashMap.Entry<String, String> result : minFlightTimeForCarrier.entrySet()) {
            System.out.println(result.getKey() + ": " + result.getValue());
        }
    }
}
