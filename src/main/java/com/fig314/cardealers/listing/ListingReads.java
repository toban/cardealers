package com.fig314.cardealers.listing;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import reactor.core.publisher.Flux;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListingReads {

    static public Flux<Listing> readCSVList(String csv) {
        try {
            Reader reader = new StringReader(csv);
            List<Listing> list = new ArrayList<>();
            CSVReaderHeaderAwareBuilder csvReaderBuilder = (CSVReaderHeaderAwareBuilder) new CSVReaderHeaderAwareBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').withQuoteChar('"').build());

            CSVReaderHeaderAware csvReader = csvReaderBuilder.build();


            Map<String, String> line = null;

            while ((line = csvReader.readMap()) != null) {
                list.add(new Listing(
                        null,
                        null,
                        line.get("code"),
                        line.get("make/model").split("/")[0],
                        line.get("make/model").split("/")[1],
                        Integer.parseInt(line.get("power-in-ps")),
                        Integer.parseInt(line.get("year")),
                        line.get("color"),
                        new BigDecimal(line.get("price"))
                ));
            }

            reader.close();
            csvReader.close();
            return Flux.fromIterable(list);
        } catch (Exception e) {
            return Flux.error(e);
        }
    }


}
