package As;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class EchoHandlerTest {

	@Test
	void testMySqlConnection() {
		assertEquals(new ConnectionMysql().userAccountExsits("chencong"), true);
	}
	@Test
	void testUnpack() {
		String[] strArr = "000001000 chencong T001 0531112320".split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "HEAD", "USERACCOUNT", "TGS_NAME", "TIME" };
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
			System.out.println(key[i]+" "+strArr[i]);
		}
	}

}
