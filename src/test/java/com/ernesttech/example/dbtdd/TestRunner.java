package com.ernesttech.example.dbtdd;

import com.ernesttech.example.dbtdd.domain.jooq.generated.tables.records.TestsRecord;
import com.ernesttech.example.dbtdd.managers.TestQueryManager;
import com.ernesttech.example.dbtdd.managers.TestsManager;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRunner {

    @Autowired
    private TestsManager testManager;

    @Autowired
    private TestQueryManager testQueryManager;

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

    @Test
    public void run() {

        for (TestsRecord testsRecord : testCases) {
            Result<Record> results = null;

            try {
                results = testQueryManager.executeQuery(testsRecord.getTest());

                String expected = String.valueOf(testsRecord.getExpected());

                String result = String.valueOf(results.getValues(0).get(0));

                assertThat(result, is(expected));

            } catch (Exception e) {
                System.out.println(buildFailureMessage(results, testsRecord, e));
            }

        }

    }

    private String buildFailureMessage(final List<?> record, final Record expected, final Exception exception) {
        return new StringBuilder()
                .append("ERROR - ")
                .append("Got: \n")
                .append(record)
                .append("\n")
                .append("Expected: \n")
                .append(expected)
                .append("\n")
                .append("Exception: ")
                .append(exception)
                .toString();
    }

}