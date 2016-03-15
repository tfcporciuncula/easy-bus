package com.arctouch.easybus.data;

import com.arctouch.easybus.route.stops.Stop;
import com.arctouch.easybus.route.stops.StopsResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * It tests the mapping for the classes Stop and StopsResult with their JSON equivalents.
 */
public class StopJsonMappingTest {

    // this is the response when querying 'findStopsByRouteId' with '35' as parameter
    private static final String SOME_STOPS_RESPONSE =
            "{\"rows\":[" +
                    "{\"id\":1,\"name\":\"TICEN\",\"sequence\":1,\"route_id\":35}," +
                    "{\"id\":2,\"name\":\"RUA ANTÔNIO PEREIRA OLIVEIRA NETO\",\"sequence\":2,\"route_id\":35}," +
                    "{\"id\":3,\"name\":\"AVENIDA OSVALDO RODRIGUES CABRAL\",\"sequence\":3,\"route_id\":35}," +
                    "{\"id\":4,\"name\":\"AVENIDA JORNALISTA RUBENS DE ARRUDA RAMOS\",\"sequence\":4,\"route_id\":35}," +
                    "{\"id\":5,\"name\":\"AVENIDA GOVERNADOR IRINEU BORNHAUSEN\",\"sequence\":5,\"route_id\":35}," +
                    "{\"id\":6,\"name\":\"AVENIDA PROFESSOR HENRIQUE DA SILVA FONTES\",\"sequence\":6,\"route_id\":35}," +
                    "{\"id\":7,\"name\":\"RUA PROFESSORA MARIA FLORA PAUSEWANG\",\"sequence\":7,\"route_id\":35}," +
                    "{\"id\":8,\"name\":\"RUA ROBERTO SAMPAIO GONZAGA\",\"sequence\":8,\"route_id\":35}," +
                    "{\"id\":9,\"name\":\"RUA DELFINO CONTI\",\"sequence\":9,\"route_id\":35}," +
                    "{\"id\":10,\"name\":\"RUA DEPUTADO ANTÔNIO EDU VIEIRA\",\"sequence\":10,\"route_id\":35}," +
                    "{\"id\":11,\"name\":\"VIA EXPRESSA TÚNEL\",\"sequence\":11,\"route_id\":35}," +
                    "{\"id\":12,\"name\":\"AVENIDA GOVERNADOR GUSTAVO RICHARD\",\"sequence\":12,\"route_id\":35}]," +
            "\"rowsAffected\":0}";

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private List<Stop> stops;
    private StopsResult someStopsResult;

    @Before
    public void setup() {
        stops = Arrays.asList(
                new Stop(1, "TICEN", 1, 35),
                new Stop(2, "RUA ANTÔNIO PEREIRA OLIVEIRA NETO", 2, 35),
                new Stop(3, "AVENIDA OSVALDO RODRIGUES CABRAL", 3, 35),
                new Stop(4, "AVENIDA JORNALISTA RUBENS DE ARRUDA RAMOS", 4, 35),
                new Stop(5, "AVENIDA GOVERNADOR IRINEU BORNHAUSEN", 5, 35),
                new Stop(6, "AVENIDA PROFESSOR HENRIQUE DA SILVA FONTES", 6, 35),
                new Stop(7, "RUA PROFESSORA MARIA FLORA PAUSEWANG", 7, 35),
                new Stop(8, "RUA ROBERTO SAMPAIO GONZAGA", 8, 35),
                new Stop(9, "RUA DELFINO CONTI", 9, 35),
                new Stop(10, "RUA DEPUTADO ANTÔNIO EDU VIEIRA", 10, 35),
                new Stop(11, "VIA EXPRESSA TÚNEL", 11, 35),
                new Stop(12, "AVENIDA GOVERNADOR GUSTAVO RICHARD", 12, 35)
        );

        someStopsResult = new StopsResult();
        someStopsResult.setRows(stops);
        someStopsResult.setRowsAffected(0);
    }

    @Test
    public void testSerialization() {
        String serialized = gson.toJson(someStopsResult);
        assertThat(serialized, equalTo(SOME_STOPS_RESPONSE));
    }

    @Test
    public void testDeserialization() {
        StopsResult deserialized = gson.fromJson(SOME_STOPS_RESPONSE, StopsResult.class);
        assertThat(deserialized, equalTo(someStopsResult));
    }

}
