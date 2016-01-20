import com.github.tomakehurst.wiremock.WireMockServer
import static com.github.tomakehurst.wiremock.client.WireMock.*
import spock.lang.Shared

/**
 * Created by mhollins on 1/14/16.
 */
class statusSpec extends spock.lang.Specification {
    @Shared WireMockServer wireMockServer = new WireMockServer(); //No-args constructor will start on port 8080, no HTTPS

    def sf = new StockFighter()

    def setupSpec() {
        wireMockServer.start()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    def setup() {
        wireMockServer.stubFor(get(urlPathMatching("/venues/$sf.venue/stocks/$sf.symbol/quote"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""{
                    "ask":1000,
                    "askDepth":100,
                    "askSize":1903648525,
                    "bid":12344,
                    "bidDepth":1620162,
                    "bidSize":33088,
                    "last":12345,
                    "lastSize":6824,
                    "lastTrade":"2016-01-19T22:47:57.204766638Z",
                    "ok":true,
                    "quoteTime":"2016-01-19T22:49:49.647073275Z",
                    "symbol":"FOOBAR",
                    "venue":"TESTEX"
                }""")

        ))

        sf.baseUrl = "http://localhost:8080/"
    }

    def cleanup() {
        reset()
    }
    def "get the quote for a stock"() {
        when:
            def quote = sf.quote()
            println quote
        then:
            quote != null
            quote.ask == 1000
    }
}
