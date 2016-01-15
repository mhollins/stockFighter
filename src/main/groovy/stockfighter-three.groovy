@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.2')
@Grab('org.java-websocket:Java-WebSocket:1.3.1-SNAPSHOT')
import groovyx.net.http.RESTClient
import org.java_websocket.client.WebSocketClient

//import org.java_websocket.client.DefaultSSLWebSocketClientFactory
import org.java_websocket.handshake.ServerHandshake

import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

import static groovyx.net.http.ContentType.JSON

def apikey = ""
def venue = "TESTEX"
def symbol = "FOOBAR"
def account = "EXB123456"

def baseUrl = 'https://api.stockfighter.io/ob/api/'
def stockFighter = new RESTClient(baseUrl)
stockFighter.setProxy('157.127.239.146', 80, 'http')
stockFighter.setHeaders(["X-Starfighter-Authorization": "70808a546ce227c5f1d7eadd614c96f33d514bd7"])

def get = { method -> 
	def resp = stockFighter.get(path: "$method")
	assert resp.status == 200
	return resp.data
}

def post = { method, body ->
	def resp = stockFighter.post(
		path: "$method", body: body, 
		requestContentType: JSON )
	assert resp.status == 200
	return resp.data
}

class MyWebSocketClient extends WebSocketClient {

	MyWebSocketClient(URI serverURI) {
		super(serverURI, new org.java_websocket.drafts.Draft_17())
	}

	@Override
	void onOpen(ServerHandshake serverHandshake) {
		println 'open'
	}

	@Override
	void onMessage(String s) {
		println "message: $s"
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
if (!get("heartbeat").ok)
	println "Heartbeat not ok, possible server problem"

def uriString = "wss://api.stockfighter.io/ob/api/ws/$account/venues/$venue/tickertape"
println "attempting to connect to: $uriString"
URI uri = new URI(uriString)
//WebSocketImpl.DEBUG = true
MyWebSocketClient client = new MyWebSocketClient(uri)
try {
	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("157.127.239.146", 80))
	SSLContext sslContext = null;
	Socket proxySocket = new Socket(proxy)
	String host = uri.getHost()
	println "host= $host"
	int port = 443
	proxySocket.connect(new InetSocketAddress(host, port))
	if (proxySocket.isConnected())
		println "connected."
	sslContext = SSLContext.getInstance("TLS")
	sslContext.init(null, null, null)
	SSLSocketFactory factory = sslContext.getSocketFactory()
	client.setSocket(factory.createSocket(proxySocket, host, port, true))

} catch (Exception e) {
	println "sslContext exception"
	e.printStackTrace()
}
//client.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sslContext))
client.connectBlocking()

BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
while (true) {
	String line = reader.readLine()
	if (line.equals("close")) {
		client.close()
		break
	} else {
		client.send(line)
	}
}
