package liauw.scalajs

import scala.scalajs.js.JSApp

import liauw.scalajs.games.Snake

object Main extends JSApp {
  def main(): Unit = {
    Snake.addToElement("target")
  }
}
