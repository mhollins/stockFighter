import groovyx.net.http.RESTClient
import groovy.json.JsonSlurper

import static groovyx.net.http.ContentType.JSON

/**
 * Created by mhollins on 1/19/16.
 */
class StockFighter {

    def apikey = "70808a546ce227c5f1d7eadd614c96f33d514bd7"
    def venue = "TESTEX"
    def symbol = "FOOBAR"
    def account = "EXB123456"
    def proxy = false

    def baseUrl = 'https://api.stockfighter.io/ob/api/'
    def client = new RESTClient(baseUrl)

    StockFighter() {
        if (proxy)
            client.setProxy('157.127.239.146', 80, 'http')
        client.setHeaders(["X-Starfighter-Authorization": "$apikey"])
    }
    void setBaseUrl(String url) {
        baseUrl = url
        client.setUri(baseUrl)
    }

    def get = { method ->
        def resp = client.get(path: "$method")
        assert resp.status == 200
        return resp.data
    }

    def post = { method, body ->
        def resp = client.post(
                path: "$method", body: body,
                requestContentType: JSON )
        assert resp.status == 200
        return resp.data
    }

    def delete = { method ->
        def resp = client.delete( path: "$method" )
        assert resp.status == 200
        return resp.data
    }

    def order = { order ->
        post("venues/$venue/stocks/$symbol/orders", order)
    }

    def getOrders = { orderId ->
        if (orderId)
            return get ("venues/$venue/stocks/$symbol/orders/$orderId")

        get("venues/$venue/accounts/$account/orders").orders
    }

    def quote = {
        println "Getting quote for $symbol"
        get("venues/$venue/stocks/$symbol/quote")
    }
}
