package uk.gov.hmcts.multipba.scenario

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import uk.gov.hmcts.multipba.util._
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.{BufferedWriter, FileWriter}
import java.util.UUID.randomUUID
import scala.util.Random

object CreateOrg {

	val CreateNewOrg = 

    exec(_.setAll(
      ("FirstName",Common.randomString(10)),
      ("LastName",Common.randomString(10)),
      ("RandDigits",Common.randomString(5).toUpperCase()),
      ("RandPBA1",Common.randomString(3).toUpperCase()),
      ("RandPBA2",Common.randomString(3).toUpperCase()),
      ("RandPBA3",Common.randomString(3).toUpperCase()),
      "currentDate" -> Common.now.format(Common.patternDate),
      "currentTime" -> Common.now.format(Common.patternTime)
    ))

    .group("CreateOrg_010_HomePage") {
      exec(http("CreateOrg_010_005_RegisterHomePage")
        .get("/register-org/register")
        .headers(Environment.commonHeader))

      .exec(getCookieValue(CookieKey("XSRF-TOKEN").saveAs("XSRFToken")))

      .exec(http("CreateOrg_010_010_RegisterHomePage")
        .get("/external/configuration-ui/")
        .headers(Environment.commonHeader))
    }

		.pause(Environment.thinkTime)

		.exec(http("CreateOrg_020_SubmitNewOrgRegistration")
			.post("/external/register-org/register")
			.headers(Environment.commonHeader)
      .header("content-type", "application/json")
      .header("x-xsrf-token", "${XSRFToken}")
			.body(ElFileBody("bodies/CreateNewOrg.json"))
      .check(jsonPath("$.organisationIdentifier").saveAs("orgId")))

    .pause(Environment.thinkTime)

    //Outputs the newly created Org ID and Org Name
    .exec {
      session =>
        val fw = new BufferedWriter(new FileWriter("NewOrgIDs.csv", true))
        try {
          fw.write(session("orgId").as[String] + "," + "perf" + "-" + session("currentDate").as[String] + "-" + session("currentTime").as[String] + "_" + session("RandDigits").as[String] + "\r\n")
        }
        finally fw.close()
        session
    }  

}