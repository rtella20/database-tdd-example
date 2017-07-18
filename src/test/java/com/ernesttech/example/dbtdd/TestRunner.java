package com.ernesttech.example.dbtdd;

import com.ernesttech.example.dbtdd.domain.jooq.generated.tables.records.TestsRecord;
import com.ernesttech.example.dbtdd.managers.TestQueryManager;
import com.ernesttech.example.dbtdd.managers.TestsManager;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestRunner.class);

    @Autowired
    private TestsManager testManager;

    @Autowired
    private TestQueryManager testQueryManager;

    private static int successCount = 0;
    private static int failureCount = 0;
    private static int testRunCount = 0;

    private List<TestsRecord> testCases;

    @Before
    public void setUp() throws Exception {
        if (testCases == null) {
            testCases = testManager.findAllTests();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void afterClass() {
        LOGGER.info("\n\nSTATUS: {}\n\nTotal Tests Run: {}\n" +
                        "Successful tests: {}\n" +
                        "Failed tests: {}\n\n", failureCount == 0 ? "Success" : "Failure",
                testRunCount, successCount, failureCount);
    }

    @Test
    public void runTests() {

        for (TestsRecord testsRecord : testCases) {
            testRunCount++;

            Result<Record> results = null;

            try {
                results = testQueryManager.executeQuery(testsRecord.getTest());

                String expected = String.valueOf(testsRecord.getExpected());

                String result = String.valueOf(results.getValues(0).get(0));

                if (expected.equalsIgnoreCase(result)) {
                    successCount++;
                } else {
                    LOGGER.error(buildFailureMessage(results, testsRecord, null));
                    failureCount++;
                }

            } catch (Exception e) {

                LOGGER.error(buildFailureMessage(results, testsRecord, e));

                failureCount++;
            }

        }

    }

    private String buildFailureMessage(final List<?> record, final TestsRecord expected, final Exception exception) {
        return new StringBuilder()
                .append("\n\nERROR - ")
                .append("Got: \n")
                .append(record)
                .append("\n\n")
                .append("Expected: \n")
                .append(expected.getExpected())
                .append("\n\n")
                .append("Full Record Returned: \n")
                .append(expected)
                .append("\n\n")
                .append("Exception: ")
                .append(exception)
                .append("\n\n")
                .toString();
    }

}