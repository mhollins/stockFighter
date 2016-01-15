@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.2')
import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.*

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

def quote = get("venues/$venue/stocks/$symbol/quote")
println "$quote.quoteTime // $venue // $symbol"
println "\tbid: $quote.bid \tdepth: $quote.bidDepth \tsize: $quote.bidSize"
println "\task: $quote.ask \tdepth: $quote.askDepth \tsize: $quote.askSize"
println "\tlast: $quote.lastSize @ $quote.last \ttime: $quote.lastTrade"
