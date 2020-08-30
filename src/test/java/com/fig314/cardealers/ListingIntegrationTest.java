package com.fig314.cardealers;

import com.fig314.cardealers.listing.data.Dealer;
import com.fig314.cardealers.listing.repository.DealerRepository;
import com.fig314.cardealers.listing.data.Listing;
import com.fig314.cardealers.listing.repository.ListingRepository;
import com.mongodb.reactivestreams.client.MongoClient;
import lombok.SneakyThrows;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.FileCopyUtils;
import reactor.test.StepVerifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;

import static java.nio.charset.StandardCharsets.UTF_8;


//@DataMongoTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListingIntegrationTest {

    @LocalServerPort
    private int port;

    private WebTestClient webClient;

    @Autowired
    ListingRepository listingRepository;

    @Autowired
    DealerRepository dealerRepository;

    @Autowired
    private MongoClient _mongo;

    @Autowired
    ReactiveMongoTemplate template;

    @Autowired
    private ResourceLoader resourceLoader;


    @SneakyThrows
    @BeforeAll
    public void setup() {
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + this.port + "/vehicle")
                .build();

        String listingSchema = readResource("/listingValidator.json");
        String dealerSchema = readResource("/dealerValidator.json");

        template
                .createCollection(
                        "listing",
                        CollectionOptions.empty()
                                .schema(
                                        MongoJsonSchema.of(Document.parse(
                                                listingSchema
                                        ))))
                .block();

        template
                .createCollection(
                        "dealer",
                        CollectionOptions.empty()
                                .schema(
                                        MongoJsonSchema.of(Document.parse(
                                                dealerSchema
                                        ))))
                .block();

        this.dealerRepository.insert(new Dealer( "5f355639568a6b917ff89de0")).block();
        this.dealerRepository.insert(new Dealer( "5f355624568a6b917ff89ddc")).block();
        this.dealerRepository.insert(new Dealer( "5f3555b1568a6b917ff89dcd")).block();

    }

    @SneakyThrows
    public String readResource(String path) {
        InputStream is = getClass().getResource(path).openStream();
        Reader reader = new InputStreamReader(is, UTF_8);
        String rslt =  FileCopyUtils.copyToString(reader);
        is.close();

        return rslt;
    }

    @Test
    public void postSequence() {


        // zero elements at start
        this.webClient.get()
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Listing.class).hasSize(0);

        // a few vendors
        StepVerifier.create(this.dealerRepository.findAll())
                .expectNextCount(3)
                .expectComplete()
                .verify();

        // unexistant vendor, correct body -> 404
        this.webClient.post().uri("/dealer/123")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[\n" +
                        "{\n" +
                        "\"code\": \"a\",\n" +
                        "\"make\": \"renault\",\n" +
                        "\"model\": \"megane\",\n" +
                        "\"kw\": 132,\n" +
                        "\"year\": 2014,\n" +
                        "\"color\": \"red\",\n" +
                        "\"price\": 13990\n" +
                        "}\n" +
                        "]")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();

        // right vendor, correct body -> 200
        this.webClient.post().uri("/dealer/5f355639568a6b917ff89de0")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[\n" +
                        "  {\n" +
                        "    \"code\": \"a\",\n" +
                        "    \"make\": \"renault\",\n" +
                        "    \"model\": \"megane\",\n" +
                        "    \"year\": 2014,\n" +
                        "    \"color\": \"red\",\n" +
                        "    \"price\": 13990,\n" +
                        "    \"kw\": 132\n" +
                        "  },\n" +
                        "  {   \n" +
                        "    \"code\": \"b\",\n" +
                        "    \"make\": \"renault\",\n" +
                        "    \"model\": \"logan\",\n" +
                        "    \"year\": 2003,\n" +
                        "    \"color\": \"red\",\n" +
                        "    \"price\": 13990,\n" +
                        "    \"kw\": 132\n" +
                        "  },\n" +
                        "  {   \n" +
                        "    \"code\": \"c\",\n" +
                        "    \"make\": \"renault\",\n" +
                        "    \"model\": \"megane\",\n" +
                        "    \"year\": 2014,\n" +
                        "    \"color\": \"red\",\n" +
                        "    \"price\": 13990,\n" +
                        "    \"kw\": 132\n" +
                        "  }\n" +
                        "]")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();

        // other right vendor, correct body -> 200
        this.webClient.post().uri("/dealer/5f355624568a6b917ff89ddc")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[\n" +
                        "  {\n" +
                        "    \"code\": \"a\",\n" +
                        "    \"make\": \"renault\",\n" +
                        "    \"model\": \"megane\",\n" +
                        "    \"year\": 2014,\n" +
                        "    \"color\": \"red\",\n" +
                        "    \"price\": 13990,\n" +
                        "    \"kw\": 132\n" +
                        "  }\n" +
                        "]")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();

        this.webClient.get()
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Listing.class).hasSize(4);

        //first dealer has 3
        this.webClient.get().uri("/dealer/5f355639568a6b917ff89de0")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Listing.class).hasSize(3);

        //second dealer has 1
        this.webClient.get().uri("/dealer/5f355624568a6b917ff89ddc")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Listing.class).hasSize(1);

        // a for first dealer was inserted
        StepVerifier.create(this.listingRepository.findAllByDealer("5f355639568a6b917ff89de0").filter(q -> q.getCode().equals("a")))
                .expectNextMatches( q -> {
                    q.setId(null);

                    return q.equals(new Listing(null, "5f355639568a6b917ff89de0", "a", "renault", "megane", 132, 2014, "red", new BigDecimal(13990)));
                })
                .expectComplete()
                .verify();

        // b for first dealer was inserted
        StepVerifier.create(this.listingRepository.findAllByDealer("5f355639568a6b917ff89de0").filter(q -> q.getCode().equals("b")))
                .expectNextMatches( q -> {
                    q.setId(null);

                    return q.equals(new Listing(null, "5f355639568a6b917ff89de0", "b", "renault", "logan", 132, 2003, "red", new BigDecimal(13990)));
                })
                .expectComplete()
                .verify();

        // updating a for first dealer
        this.webClient.post().uri("/dealer/5f355639568a6b917ff89de0")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[\n" +
                        "  {\n" +
                        "    \"code\": \"a\",\n" +
                        "    \"make\": \"renault\",\n" +
                        "    \"model\": \"megane\",\n" +
                        "    \"year\": 2015,\n" +
                        "    \"color\": \"red\",\n" +
                        "    \"price\": 13991,\n" +
                        "    \"kw\": 133\n" +
                        "  }\n" +
                        "]")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();

        // a was updated for first dealer
        StepVerifier.create(this.listingRepository.findAllByDealer("5f355639568a6b917ff89de0").filter(q -> q.getCode().equals("a")))
                .expectNextMatches( q -> {
                    q.setId(null);

                    return q.equals(new Listing(null, "5f355639568a6b917ff89de0", "a", "renault", "megane", 133, 2015, "red", new BigDecimal(13991)));
                })
                .expectComplete()
                .verify();

        // b for first dealer is unchanged
        StepVerifier.create(this.listingRepository.findAllByDealer("5f355639568a6b917ff89de0").filter(q -> q.getCode().equals("b")))
                .expectNextMatches( q -> {
                    q.setId(null);

                    return q.equals(new Listing(null, "5f355639568a6b917ff89de0", "b", "renault", "logan", 132, 2003, "red", new BigDecimal(13990)));
                })
                .expectComplete()
                .verify();

        // a for second dealer is unchanged
        StepVerifier.create(this.listingRepository.findAllByDealer("5f355624568a6b917ff89ddc").filter(q -> q.getCode().equals("a")))
                .expectNextMatches( q -> {
                    q.setId(null);

                    return q.equals(new Listing(null, "5f355624568a6b917ff89ddc", "a", "renault", "megane", 132, 2014, "red", new BigDecimal(13990)));
                })
                .expectComplete()
                .verify();

        //test search - only one logan
        this.webClient.get().uri("/search?make=renault&model=logan")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Listing.class).hasSize(1);

        //test search - three megane
        this.webClient.get().uri("/search?make=renault&model=megane")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Listing.class).hasSize(3);

        //test csv upload - wrong format
        this.webClient.post().uri("/dealer/5f3555b1568a6b917ff89dcd/csv")
                .bodyValue("code,make/model,power-in-ps,year,color,price\n" +
                        "1,mercedes/a 180,123,2014,black,15950\n" +
                        "2,audi/a3,111,2016,white,17210\n" +
                        "3,vw/golf,86,2018,green,14980\n" +
                        "4,skoda/octavia,86,2018,16990")
                .exchange()
                .expectStatus().is4xxClientError();

        // was not inserted
        this.webClient.get().uri("/dealer/5f3555b1568a6b917ff89dcd")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Listing.class).hasSize(0);

        //test csv upload - right format
        this.webClient.post().uri("/dealer/5f3555b1568a6b917ff89dcd/csv")
                .bodyValue("code,make/model,power-in-ps,year,color,price\n" +
                        "1,mercedes/a 180,123,2014,black,15950\n" +
                        "2,audi/a3,111,2016,white,17210\n" +
                        "3,vw/golf,86,2018,green,14980\n" +
                        "4,skoda/octavia,86,2018,orange,16990")
                .exchange()
                .expectStatus().is2xxSuccessful();

        // was inserted
        this.webClient.get().uri("/dealer/5f3555b1568a6b917ff89dcd")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Listing.class).hasSize(4);



    }

}