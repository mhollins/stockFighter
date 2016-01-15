@Grab('org.java-websocket:Java-WebSocket:1.3.1-SNAPSHOT')
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

def apikey = ""
def venue = "TESTEX"
def symbol = "FOOBAR"
def account = "EXB123456"
def baseUrl = 'https://api.stockfighter.io/ob/api/'

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


