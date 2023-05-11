# manageorg-performance
UI performance tests for Manage Org

To run locally:
- Performance test against the perftest environment: `./gradlew gatlingRun`

Flags:
- Debug (single-user mode): `-Ddebug=on e.g. ./gradlew gatlingRun -Ddebug=on`
- Run against AAT: `Denv=aat e.g. ./gradlew gatlingRun -Denv=aat`

URL to register a new org: `https://manage-org.{env}}.platform.hmcts.net/register-org/register`
