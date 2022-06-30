package uk.gov.hmcts.multipba.scenario

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import uk.gov.hmcts.multipba.util._

object ApproveOrg {

	val ApproveNewOrg = 

    //Login screen
    exec(http("request_0")
      .get("/")
      .headers(Environment.commonHeader)
      .resources(http("request_1")
      .get("/api/environment/config")
      .headers(Environment.commonHeader),
            http("request_2")
      .get("/api/environment/config")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"),
            http("request_3")
      .get("/api/user/details")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"),
            http("request_4")
      .get("/auth/login")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*")))

    .pause(4)

    //Login
    .exec(http("request_6")
      .post(Environment.idamURL + "/login?client_id=xuiaowebapp&redirect_uri=https://administer-orgs.perftest.platform.hmcts.net/oauth2/callback&state=nxyFLbdXzDXuPXOT4Q4WtgdpH3mtIEuGGIpkgaoiIWo&nonce=ZTkh-AdVcuRfPhcSwECikhNfiBNDTU5jhQZJK4c9a1w&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user&prompt=")
      .headers(Environment.commonHeader)
      .formParam("username", "vmuniganti@mailnesia.com")
      .formParam("password", "Monday01")
      .formParam("save", "Sign in")
      .formParam("selfRegistrationEnabled", "false")
      .formParam("mojLoginEnabled", "true")
      .formParam("_csrf", "3cda1c24-b9df-4ffb-9a7b-895ccac8867c"))

    .exec(http("request_7")
      .get("/api/environment/config")
      .headers(Environment.commonHeader))

    .exec(http("request_8")
      .get("/api/environment/config")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_9")
      .get("/api/user/details")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_10")
      .get("/auth/isAuthenticated")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_13")
      .post("/api/organisations?status=PENDING,REVIEW")
      .headers(Environment.commonHeader)
      .header("x-xsrf-token", "PPgtVgv5-rcPparwhqxY1l-NNY1kAlHJSSqc")
      .body(RawFileBody("approveOrg1_0013_request.txt")))
      

    .pause(11)

    //Search org
    .exec(http("request_15")
      .post("/api/organisations?status=PENDING,REVIEW")
      .headers(Environment.commonHeader)
      .header("x-xsrf-token", "PPgtVgv5-rcPparwhqxY1l-NNY1kAlHJSSqc")
      .body(RawFileBody("approveOrg1_0015_request.txt")))

    .pause(4)

    //View org
    .exec(http("request_16")
      .get("/auth/isAuthenticated")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_17")
      .get("/api/organisations?organisationId=3NHUNYK")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_18")
      .get("/api/organisations?usersOrgId=3NHUNYK")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(status.is(500)))

    .exec(http("request_19")
      .get("/api/monitoring-tools")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_20")
      .get("/api/pbaAccounts/?accountNames=PBA1042AAA,PBA1042AAC,PBA1042AAB")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_21")
      .get("/api/pbaAccounts/?accountNames=PBA1042AAA,PBA1042AAC,PBA1042AAB,PBA1042AAA,PBA1042AAC,PBA1042AAB")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_22")
      .get("/auth/isAuthenticated")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_23")
      .get("/api/organisations?organisationId=3NHUNYK")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))
      
    .pause(11)

    //Add new PBA
    .exec(http("request_24")
      .put("/api/updatePba")
      .headers(Environment.commonHeader)
      .header("x-xsrf-token", "PPgtVgv5-rcPparwhqxY1l-NNY1kAlHJSSqc")
      .body(RawFileBody("approveOrg1_0024_request.txt")))

    .exec(http("request_25")
      .get("/auth/isAuthenticated")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_26")
      .get("/api/organisations?organisationId=3NHUNYK")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_27")
      .get("/api/organisations?usersOrgId=3NHUNYK")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(status.is(500)))

    .exec(http("request_28")
      .get("/api/pbaAccounts/?accountNames=PBA1042AAD,PBA1042AAA,PBA1042AAC,PBA1042AAB")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .pause(15)

    .exec(http("request_29")
      .get("/auth/isAuthenticated")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .pause(7)

    //Approve Org
    .exec(http("request_30")
      .put("/api/organisations/3NHUNYK")
      .headers(Environment.commonHeader)
      .header("x-xsrf-token", "PPgtVgv5-rcPparwhqxY1l-NNY1kAlHJSSqc")
      .body(RawFileBody("approveOrg1_0030_request.txt")))

    .exec(http("request_31")
      .get("/auth/isAuthenticated")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_33")
      .post("/api/organisations?status=PENDING,REVIEW")
      .headers(Environment.commonHeader)
      .header("x-xsrf-token", "PPgtVgv5-rcPparwhqxY1l-NNY1kAlHJSSqc")
      .body(RawFileBody("approveOrg1_0033_request.txt")))

    .pause(5)

    //View orgs
    .exec(http("request_34")
      .get("/auth/isAuthenticated")
      .headers(Environment.commonHeader)
      .header("accept", "application/json, text/plain, */*"))

    .exec(http("request_37")
      .post("/api/organisations?status=PENDING,REVIEW")
      .headers(Environment.commonHeader)
      .header("x-xsrf-token", "PPgtVgv5-rcPparwhqxY1l-NNY1kAlHJSSqc")
      .body(RawFileBody("approveOrg1_0037_request.txt")))

    //Logout
    .exec(http("request_38")
      .get("/auth/logout")
      .headers(Environment.commonHeader))

}