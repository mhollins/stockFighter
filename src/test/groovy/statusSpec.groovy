import com.github.tomakehurst.wiremock.WireMockServer
import spock.lang.Shared

/**
 * Created by mhollins on 1/14/16.
 */
class statusSpec extends spock.lang.Specification {
    @Shared WireMockServer wireMockServer = new WireMockServer(); //No-args constructor will start on port 8080, no HTTPS



    def setupSpec() {
        wireMockServer.start()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    def "length of Spock's and his friends' names"() {
        expect:
        name.size() == length

        where:
        name     | length
        "Spock"  | 5
        "Kirk"   | 4
        "Scotty" | 6
    }
}
