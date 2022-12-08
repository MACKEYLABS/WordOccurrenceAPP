import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


class WordOccurrenceFinalTest {

	@Test
	@EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
	void onlyOn64BitArchitectures() {
	  //test for 64 bit architecture
    }
	@Test
	public void initializeWithPutIfAbsent() {  
	    Map<String, Integer> linkedHashMap = new LinkedHashMap<>();

	    // Add elements to LinkedHashMap
	    linkedHashMap.putIfAbsent("the", 1);
	    linkedHashMap.putIfAbsent("raven", 2);
	    linkedHashMap.putIfAbsent("lives", 3);

	    // Can add null key and value
	    linkedHashMap.putIfAbsent(null, 4);
	    linkedHashMap.putIfAbsent("on", null);

	    // Duplicate key will be ignored
	    linkedHashMap.putIfAbsent("the", 10);

	    // The output ordering is predictable as LinkedHashMap is reserved the insertion order
	    System.out.println(linkedHashMap);
	}
	@Test
    public void test(){
        Map<String, Integer> act1 = new LinkedHashMap<>();
        act1.put("the", 56);
        act1.put("and", 30);
        act1.put("my", 24);
        act1.put("between", null);
        act1.put("Raven", 8);
        act1.put("crow", 3);
        act1.put("minute", 102);
        act1.put("ancient", 10); 

        Map<String, Integer> exp1 = new LinkedHashMap<>();
        exp1.put("ancient", 10);
        exp1.put("Raven", 8);
        exp1.put("minute", 102);
        exp1.put("the", 56);  
        exp1.put("crow", 3);
        exp1.put("my", 24);
        exp1.put("and", 30);
        exp1.put("between",null);
 
        exp1.entrySet().stream().forEach(n->System.out.println(n.getKey()));
        act1.entrySet().stream().forEach(n->System.out.println(n.getKey()));
        assertEquals(exp1, act1);
        assertTrue(act1.entrySet().equals(exp1.entrySet()));
    }
        

	@Test
	public void testAssertionRaven() {
	    String expected = "the";
	    String actual = "the";
	    assertEquals(expected, actual);
	    assertEquals("Terminate - Strings do not match!", expected, actual);
	}
	
	
	@Test
	public void testingTheRavenNULLValues() {
	    Integer[] expectedCode = null;
	    Integer[] actualCode = null;
	    assertArrayEquals(expectedCode, actualCode);
	}
}
