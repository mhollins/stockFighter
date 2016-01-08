import groovy.json.JsonSlurper

def apikey = ""
//def venue = "CMIEX"
//def symbol = "DQU"
def venue = "TESTEX"
def symbol = "FOOBAR"
def account = "EXB123456"

def baseUrl = "https://api.stockfighter.io/ob/api/"
def slurper = new JsonSlurper()

def get = { method -> 
	def httpConnection = new URL(baseUrl + method).openConnection()
	assert httpConnection.responseCode == httpConnection.HTTP_OK
	slurper.parse(httpConnection.inputStream.newReader())
}
def post = { method, body ->

}

println get( "venues/$venue/stocks/$symbol" )
