package securitydigest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderValueParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DigestTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private Map<String, String> invokeFirstRequest(String uri, Header requestheader) {
		Map<String, String> result = null;
		HttpClient client = new DefaultHttpClient();

		try {
			HttpGet httpGet = new HttpGet(uri);
			httpGet.addHeader(requestheader);
			HttpResponse response = client.execute(httpGet);

			int status = response.getStatusLine().getStatusCode();
			System.out.println("Status : " + status);

			for (Header header : response.getAllHeaders()) {
				System.out.println(header.getName() + "--" + header.getValue());
				if (header.getName().equals("WWW-Authenticate")) {
					result = parseAuthResponse(header);
				}
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Test
	public void test() {
		String uri = "http://localhost:8080/securitydigest";
		Map<String, String> result = invokeFirstRequest(uri, new BasicHeader("", ""));
		String nc = "000001";
		String cnonce = "e10adc3949ba59abbe56e057f20f883e";
		String nonce = result.get("nonce");
		String qop = result.get("qop");
		String realm = result.get("realm");
		String response = createResponse("jay", realm, "jay", "get", uri, nonce, nc, cnonce, qop);

		String headerValue = "Digest username=\"jay\", nonce=\"" + nonce + "\", qop=\"auth\", realm=\"" + realm
				+ ", cnonce=\"" + cnonce + "\", nc=\"" + nc + "\", response=\"" + response + "\"";
		System.out.println(headerValue);
		Header authHeader = new BasicHeader("Authorisation", headerValue);

		invokeFirstRequest(uri, authHeader);

	}

	// MD5(MD5(username:realm:password):nonce:nc:cnonce:qop:MD5(method:URL))
	private String createResponse(String userName, String realm, String password, String method, String url,
			String nonce, String nc, String cnonce, String qop) {
		String response = "";
		String user = "jay";

		String part1 = DigestUtils.md5Hex(user + ":" + realm + ":" + password);
		String part2 = DigestUtils.md5Hex(method + ":" + url);
		String part3 = nonce + ":" + nc + ":" + cnonce + ":" + qop;
		response = DigestUtils.md5Hex(part1 + part3 + part2);
		System.out.println(part1 + "," + part2 +"," +response);
		return response;
	}

	private static Map<String, String> parseAuthResponse(final Header authResponse) {
		String s = authResponse.getValue();
		if (!s.startsWith("Digest ")) {
			return null;
		}
		HeaderElement[] elements = BasicHeaderValueParser.parseElements(s.substring(7), null);
		Map<String, String> map = new HashMap(elements.length);
		for (int i = 0; i < elements.length; i++) {
			HeaderElement element = elements[i];
			map.put(element.getName(), element.getValue());
		}
		return map;
	}
}
