package org.saucedemo.testextensions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(setterPrefix = "set")
public class TestRunMetaData {
    private String executionTime;
    private String testClass;
    private String testName;
    private boolean testExceptionPresent;
    private String reason;
    private String testResult;

    public TestRunMetaData getBody(ExtensionContext context) {
        executionTime = LocalDateTime.now().toString();
        testClass = context.getTestClass().get().getSimpleName();
        testName = context.getDisplayName();
        testExceptionPresent = context.getExecutionException().isPresent();
        setTestResult(testExceptionPresent);
        reason = context.getExecutionException().toString();

        return this;
    }

    private void setTestResult(boolean testStatus) {
        if (testStatus) {
            testResult = "failed";
        } else {
            testResult = "passed";
        }
    }
}
