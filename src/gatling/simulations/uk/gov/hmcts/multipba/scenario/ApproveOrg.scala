package uk.gov.hmcts.multipba.scenario

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import uk.gov.hmcts.multipba.util._
import java.util.UUID.randomUUID
import scala.util.Random

object ApproveOrg {

  val AdminUrl = Environment.adminUrl
  val adminusers = csv("AdminOrgUsers.csv")

  private val rng: Random = new Random()

	val ApproveOrgHomepage = 

    //Login screen
    group("AdminOrg_010_Homepage") {
      exec(http("AdminOrg_010_005_Homepage")
        .get(Environment.adminUrl + "/")
        .headers(Environment.commonHeader))

      .exec(http("AdminOrg_010_010_Homepage")
        .get(Environment.adminUrl + "/api/environment/config")
        .headers(Environment.commonHeader))

      .exec(http("AdminOrg_010_015_Homepage")
        .get(Environment.adminUrl + "/api/environment/config")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_010_020_Homepage")
        .get(Environment.adminUrl + "/api/user/details")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_010_025_Homepage")
        .get(Environment.adminUrl + "/auth/login")
        .headers(Environment.commonHeader)
        .header("accept-encoding", "gzip, deflate, br")
        .header("accept", "application/json, text/plain, */*")
        .check(css("input[name='_csrf']", "value").saveAs("csrfToken"))
        .check(regex("callback&state=(.*)&nonce=").saveAs("state"))
        .check(regex("&nonce=(.*)&response_type").saveAs("nonce")))

      // .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(adminDomain).saveAs("XSRFToken")))
      .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain("administer-orgs.perftest.platform.hmcts.net").saveAs("XSRFToken")))
      // .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(AdminUrl.replace("https://", "")).saveAs("XSRFToken")))
    }

    .pause(Environment.thinkTime)

  val ApproveOrgLogin =

    feed(adminusers)

    //Login
    .group("AdminOrg_020_Login") {
      exec(http("AdminOrg_020_005_Login")
        .post(Environment.idamURL + "/login?client_id=xuiaowebapp&redirect_uri=" + Environment.adminUrl + "/oauth2/callback&state=${state}&nonce=${nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user&prompt=")
        .headers(Environment.commonHeader)
        .formParam("username", "vmuniganti@mailnesia.com")
        .formParam("password", "Monday01")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "false")
        .formParam("mojLoginEnabled", "true")
        .formParam("_csrf", "${csrfToken}"))

      .exec(http("AdminOrg_020_010_Login")
        .get(Environment.adminUrl + "/api/environment/config")
        .headers(Environment.commonHeader))

      .exec(http("AdminOrg_020_015_Login")
        .get(Environment.adminUrl + "/api/environment/config")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_020_020_Login")
        .get(Environment.adminUrl + "/api/user/details")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_020_025_Login")
        .get(Environment.adminUrl + "/auth/isAuthenticated")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_020_030_Login")
        .post(Environment.adminUrl + "/api/organisations?status=PENDING,REVIEW")
        .headers(Environment.commonHeader)
        .header("content-type", "application/json")
        .header("x-xsrf-token", "${XSRFToken}")
        .body(ElFileBody("bodies/AdminOrgHomeSearch.json")))
    }
      
    .pause(Environment.thinkTime)

  val SearchOrg = 

    //Search org
    exec(http("AdminOrg_030_SearchForOrg")
      .post(Environment.adminUrl + "/api/organisations?status=PENDING,REVIEW")
      .headers(Environment.commonHeader)
      .header("content-type", "application/json")
      .header("x-xsrf-token", "${XSRFToken}")
      .body(ElFileBody("bodies/AdminOrgSearchOrg.json"))
      .check(jsonPath("$.organisations[0].organisationIdentifier").saveAs("OrgID"))
      .check(jsonPath("$.organisations[0].name").saveAs("OrgName"))
      .check(jsonPath("$.organisations[0].contactInformation[0].addressLine1").saveAs("addressLine1"))
      .check(jsonPath("$.organisations[0].contactInformation[0].townCity").saveAs("townCity"))
      .check(jsonPath("$.organisations[0].contactInformation[0].county").saveAs("county"))
      .check(jsonPath("$.organisations[0].pendingPaymentAccount[0]").saveAs("PBA1"))
      .check(jsonPath("$.organisations[0].pendingPaymentAccount[1]").saveAs("PBA2"))
      .check(jsonPath("$.organisations[0].pendingPaymentAccount[2]").saveAs("PBA3"))
      .check(jsonPath("$.organisations[0].superUser.firstName").saveAs("firstName"))
      .check(jsonPath("$.organisations[0].superUser.lastName").saveAs("lastName"))
      .check(jsonPath("$.organisations[0].superUser.email").saveAs("email"))
      )

    .pause(Environment.thinkTime)

  val ViewOrg = 

    //View org
    group("AdminOrg_030_ViewOrg") {
      exec(http("AdminOrg_030_005_ViewOrg")
        .get(Environment.adminUrl + "/auth/isAuthenticated")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_030_010_ViewOrg")
        .get(Environment.adminUrl + "/api/organisations?organisationId=${OrgID}")
        .headers(Environment.commonHeader)
        .header("content-type", "application/json")
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_030_015_ViewOrg")
        .get(Environment.adminUrl + "/api/organisations?usersOrgId=${OrgID}")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*")
        .check(status.in(500, 304)))

      .exec(http("AdminOrg_030_020_ViewOrg")
        .get(Environment.adminUrl + "/api/monitoring-tools")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_030_025_ViewOrg")
        .get(Environment.adminUrl + "/api/pbaAccounts/?accountNames=${PBA1},${PBA2},${PBA3}")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_030_030_ViewOrg")
        .get(Environment.adminUrl + "/api/pbaAccounts/?accountNames=${PBA1},${PBA2},${PBA3},${PBA1},${PBA2},${PBA3}")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_030_035_ViewOrg")
        .get(Environment.adminUrl + "/auth/isAuthenticated")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_030_040_ViewOrg")
        .get(Environment.adminUrl + "/api/organisations?organisationId=${OrgID}")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))
    }

    .pause(Environment.thinkTime)

  val AddNewPBA = 

    exec(_.setAll(
      ("threeLetters",Common.randomString(3).toUpperCase())
    ))

    //Add new PBA
    .group("AdminOrg_040_AddPBA") {
      exec(http("AdminOrg_040_005_AddPBA")
        .put(Environment.adminUrl + "/api/updatePba")
        .headers(Environment.commonHeader)
        .header("content-type", "application/json")
        .header("x-xsrf-token", "${XSRFToken}")
        .body(ElFileBody("bodies/AdminAddPBA.json")))

      .exec(http("AdminOrg_040_010_AddPBA")
        .get(Environment.adminUrl + "/auth/isAuthenticated")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_040_015_AddPBA")
        .get(Environment.adminUrl + "/api/organisations?organisationId=${OrgID}")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_040_020_AddPBA")
        .get(Environment.adminUrl + "/api/organisations?usersOrgId=${OrgID}")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*")
        .check(status.in(500, 304)))

      .exec(http("AdminOrg_040_025_AddPBA")
        .get(Environment.adminUrl + "/api/pbaAccounts/?accountNames=PBA${currentTime}${threeLetters},${PBA1},${PBA2},${PBA3}")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_040_030_AddPBA")
        .get(Environment.adminUrl + "/auth/isAuthenticated")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))
    }

    .pause(Environment.thinkTime)

  val ApproveNewOrg = 

    //Approve Org
    group("AdminOrg_050_ApproveOrg") {
      exec(http("AdminOrg_050_005_ApproveOrg")
        .put(Environment.adminUrl + "/api/organisations/${OrgID}")
        .headers(Environment.commonHeader)
        .header("content-type", "application/json")
        .header("x-xsrf-token", "${XSRFToken}")
        .body(ElFileBody("bodies/AdminApproveOrg.json")))

      .exec(http("AdminOrg_050_010_ApproveOrg")
        .get(Environment.adminUrl + "/auth/isAuthenticated")
        .headers(Environment.commonHeader)
        .header("accept", "application/json, text/plain, */*"))

      .exec(http("AdminOrg_050_015_ApproveOrg")
        .post(Environment.adminUrl + "/api/organisations?status=PENDING,REVIEW")
        .headers(Environment.commonHeader)
        .header("content-type", "application/json")
        .header("x-xsrf-token", "${XSRFToken}")
        .body(ElFileBody("bodies/AdminOrgSearchOrg.json")))
    }

    .pause(Environment.thinkTime)

  val Logout =

    //Logout
    exec(http("AdminOrg_060_Logout")
      .get(Environment.adminUrl + "/auth/logout")
      .headers(Environment.commonHeader))

}