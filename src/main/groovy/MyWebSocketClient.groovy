
import groovy.json.JsonSlurper
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake


class MyWebSocketClient extends WebSocketClient {
    def slurper = new JsonSlurper()

    MyWebSocketClient(URI serverURI) {
        super(serverURI, new org.java_websocket.drafts.Draft_17())
    }

    @Override
    void onOpen(ServerHandshake serverHandshake) {
        println 'open'
    }

    @Override
    void onMessage(String s) {
        //println "message: $s"
        def json = slurper.parseText(s)

        println json.quote.quoteTime
        println "\tbid: " + json.quote.bid
        println "\task: " + json.quote.ask
        println "\tlast: " + json.quote.lastSize + "@" + json.quote.last
    }

    @Override
    void onClose(int i, String s, boolean b) {
        println "closed. reason= $s"
    }

    @Override
    void onError(Exception e) {
        println "MyWebSocketClient Exception"
        e.printStackTrace()
    }
}

