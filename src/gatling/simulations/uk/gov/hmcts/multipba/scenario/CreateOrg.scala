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

  private val rng: Random = new Random()
  val patternDate = DateTimeFormatter.ofPattern("ddMMyy")
  val patternTime = DateTimeFormatter.ofPattern("HHmm")
  val now = LocalDateTime.now()
  private def firstName(): String = rng.alphanumeric.take(20).mkString
  private def lastName(): String = rng.alphanumeric.take(20).mkString

	val CreateNewOrg = 

    exec(_.setAll(
      ("FirstName",firstName()),
      ("LastName",lastName()),
      "currentDate" -> now.format(patternDate),
      "currentTime" -> now.format(patternTime)
    ))
  
    .exec(http("request_0")
			.get("/register-org/register")
			.headers(Environment.commonHeader))

    .exec(getCookieValue(CookieKey("XSRF-TOKEN")//.withDomain(Environment.BaseUrl)
      .saveAs("XSRFToken")))

    .exec(http("request_1")
			.get("/external/configuration-ui/")
			.headers(Environment.commonHeader))

		.pause(54)

		.exec(http("request_3")
			.post("/external/register-org/register")
			.headers(Environment.commonHeader)
      .header("content-type", "application/json")
      .header("x-xsrf-token", "${XSRFToken}")
			.body(ElFileBody("bodies/CreateNewOrg.json"))
      .check(jsonPath("$.organisationIdentifier").saveAs("orgId")))

    
    .exec {
      session =>
        val fw = new BufferedWriter(new FileWriter("NewOrgIDs.csv", true))
        try {
          fw.write(session("orgId").as[String] + "\r\n")
        }
        finally fw.close()
        session
    }  

}