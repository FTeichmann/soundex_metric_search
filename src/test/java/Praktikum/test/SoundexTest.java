package Praktikum.test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;
import com.carrotsearch.junitbenchmarks.h2.H2Consumer;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.semanticweb.yars.nx.parser.NxParser;
import praktikum.SoundexMetric;

import java.io.*;
import java.util.*;

@BenchmarkMethodChart(filePrefix = "benchmark-lists")
@BenchmarkHistoryChart(labelWith = LabelType.CUSTOM_KEY, maxRuns = 40)
public class SoundexTest extends AbstractBenchmark {

    private static H2Consumer consumer = getConsumer();
    private static ArrayList<String> listA, listB;
    private static double threshold;
    private static int lines;
    public static String testData;
    int length = 4;//length of the soundex code
    SoundexMetric metric = new SoundexMetric(length);

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule(consumer);

    public static HashMap<String, Object> getProperties() {
        threshold = 0.9d;
        lines = 1000;
        testData = "labels_en.nt";
        Properties prop = new Properties();
        try {
            InputStream stream = new FileInputStream("src/test/resources/testConfig.properties");
            prop.load(stream);
            stream.close();
            threshold = Double.valueOf(prop.getProperty("threshold"));
            lines = Integer.valueOf(prop.getProperty("lines"));
            testData = prop.getProperty("testData").equals("") ? testData : prop.getProperty("testData");
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        HashMap<String, Object> rtn = new HashMap<String, Object>();
        rtn.put("threshold", threshold);
        rtn.put("lines", lines);
        rtn.put("testData", testData);
        return rtn;
    }

    public static H2Consumer getConsumer() {
        getProperties();
        return new H2Consumer(
                new File("benchmarks/charts" + String.valueOf(threshold) + "/foo-db"),
                new File("benchmarks/charts" + String.valueOf(threshold)),
                String.valueOf(lines));
    }

    @BeforeClass
    public static void setUpLists () {
        listA = new ArrayList<String>();
        listB = new ArrayList<String>();
        try {
            NxParser nxp = new NxParser(new FileInputStream(testData));
            int i = 0;
            while (nxp.hasNext() && i < lines) {
                String tmp = nxp.next()[2].toN3();
                tmp = tmp.substring(1, tmp.lastIndexOf("@")-1);
                listA.add(tmp);
                listB.add(tmp);
                i++;
            }
        } catch (Exception e) {
            // nothing to do...
        }
    }
    @BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
    @Test
    public void testGetTrieSimilarity() {
		for(String word : listA){
			metric.add(word);
		}
		
		Map<String, Map<String,Float>> result = new HashMap<String, Map<String,Float>>();
		for(String word: listB){
			result.put(word, metric.TrieSearch(word, (float) threshold));
		}
		System.out.println(result);
	}
    @Ignore
    @BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
    @Test
    public void getSimpleSimilarity(){
        Map<String, Map<String,Float>> result = new HashMap<String, Map<String,Float>>();
        for(String source: listA){
            Map<String,Float> resultN = new HashMap<String,Float>();
            for(String target:listB){
                float similarity = metric.getSimpleSimilarity(source,target);
                if(similarity >= threshold){
                    resultN.put(target,similarity);
                }
            }
            result.put(source,resultN);
        }
        System.out.println(result);
    }

}
