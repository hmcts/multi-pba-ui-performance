package uk.gov.hmcts.multipba.util

import scala.util.Random

object Environment {

  val idamURL = "https://idam-web-public.${env}.platform.hmcts.net"
  val IDAMUrl = "https://idam-api.${env}.platform.hmcts.net"
  val S2SUrl = "http://rpe-service-auth-provider-${env}.service.core-compute-${env}.internal/testing-support"
  val BaseUrl = "https://manage-org.${env}.platform.hmcts.net"
  val adminUrl = "https://administer-orgs.${env}.platform.hmcts.net"

  val thinkTime = 7 //7

  val commonHeader = Map(
    "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "cache-control" -> "no-cache",
		"pragma" -> "no-cache",	"sec-ch-ua" -> """.Not/A)Brand";v="99", "Google Chrome";v="103", "Chromium";v="103""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "macOS",
		"sec-fetch-dest" -> "document",
		"sec-fetch-mode" -> "navigate",
		"sec-fetch-site" -> "none",
		"sec-fetch-user" -> "?1",
		"upgrade-insecure-requests" -> "1",
    "origin" -> Environment.BaseUrl)

}