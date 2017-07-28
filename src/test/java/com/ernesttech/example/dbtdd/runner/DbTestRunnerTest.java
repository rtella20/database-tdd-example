package com.ernesttech.example.dbtdd.runner;

import com.ernesttech.example.dbtdd.domain.jooq.generated.tables.records.TestsRecord;
import com.ernesttech.example.dbtdd.managers.TestQueryManager;
import com.ernesttech.example.dbtdd.managers.TestsManager;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DbTestRunnerTest {

    @Mock
    private TestsManager testManager;

    @Mock
    private TestQueryManager testQueryManager;

    @Mock
    private Result result;

    @InjectMocks
    private DbTestRunner dbTestRunner;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void noTestsInDbDoesNotCauseFailureTest() {
        when(testManager.findAllTests())
                .thenReturn(new ArrayList<>());

        assertThat(dbTestRunner.run(), is(true));
    }

    @Test
    public void exceptionThrownInTestResultsInFailureReported() {
        when(testQueryManager.executeQuery(anyString()))
                .thenThrow(Exception.class);

        List<TestsRecord> fakeTestList = new ArrayList<>();
        fakeTestList.add(new TestsRecord());

        when(testManager.findAllTests())
                .thenReturn(fakeTestList);

        assertThat(dbTestRunner.run(), is(false));
    }

    @Test
    public void testQueryReturningInteger0PassesWhenExpectedIsString0() {
        TestsRecord testsRecord = new TestsRecord();
        testsRecord.setExpected("0");

        List<TestsRecord> records = new ArrayList<>();
        records.add(testsRecord);

        when(testManager.findAllTests())
                .thenReturn(records);


        List<Integer> values = new ArrayList<>();
        values.add(0);

        result.set(0, values);
        when(result.getValues(0))
                .thenReturn(values);

        when(testQueryManager.executeQuery(anyString()))
                .thenReturn(result);


        assertThat(dbTestRunner.run(), is(true));

    }

    @Test
    public void testQueryBobNotEqualToDole() {
        TestsRecord testsRecord = new TestsRecord();
        testsRecord.setExpected("Bob");

        List<TestsRecord> records = new ArrayList<>();
        records.add(testsRecord);

        when(testManager.findAllTests())
                .thenReturn(records);

        List<String> values = new ArrayList<>();
        values.add("Dole");

        result.set(0, values);
        when(result.getValues(0))
                .thenReturn(values);

        when(testQueryManager.executeQuery(anyString()))
                .thenReturn(result);


        assertThat(dbTestRunner.run(), is(false));
    }

}