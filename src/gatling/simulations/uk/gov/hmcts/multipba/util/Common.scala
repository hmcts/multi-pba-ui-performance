package uk.gov.hmcts.multipba.util

import scala.util.Random
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Common {

  val rnd = new Random()
  private val rng: Random = new Random()
  val patternDate = DateTimeFormatter.ofPattern("ddMMyy")
  val patternTime = DateTimeFormatter.ofPattern("HHmm")
  val now = LocalDateTime.now()

  def randomString(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

}