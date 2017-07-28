package com.ernesttech.example.dbtdd.runner;

import com.ernesttech.example.dbtdd.domain.jooq.generated.tables.records.TestsRecord;
import com.ernesttech.example.dbtdd.managers.TestQueryManager;
import com.ernesttech.example.dbtdd.managers.TestsManager;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbTestRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbTestRunner.class);

    @Autowired
    private TestsManager testManager;

    @Autowired
    private TestQueryManager testQueryManager;


    public boolean run() {

        List<TestsRecord> testCases = testManager.findAllTests();

        return executeDbTests(testCases);

    }

    private boolean executeDbTests(final List<TestsRecord> testCases) {

        int successCount = 0;
        int failureCount = 0;
        int testRunCount = 0;

        for (TestsRecord testsRecord : testCases) {

            testRunCount++;

            Result<Record> results = null;

            try {
                results = testQueryManager.executeQuery(testsRecord.getTest());

                if (isExpectedEqualToActual(testsRecord, results)) {
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

        printStats(successCount, failureCount, testRunCount);


        return failureCount == 0;

    }

    private boolean isExpectedEqualToActual(TestsRecord testsRecord, Result<Record> results) {
        return getExpectedResult(testsRecord).equalsIgnoreCase(getActualResult(results));
    }

    private String getActualResult(final Result<Record> results) {
        return String.valueOf(results.getValues(0).get(0));
    }

    private String getExpectedResult(final TestsRecord testsRecord) {
        return String.valueOf(testsRecord.getExpected());
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

    private void printStats(final int successCount, final int failureCount, final int testRunCount) {
        LOGGER.info("\n\nSTATUS: {}\n\nTotal Tests Run: {}\n" +
                        "Successful tests: {}\n" +
                        "Failed tests: {}\n\n", failureCount == 0 ? "Success" : "Failure",
                testRunCount, successCount, failureCount);
    }


}
