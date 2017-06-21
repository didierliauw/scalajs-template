package liauw.scalajs.js

import liauw.scalajs.js.games.Snake

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    Snake.addToElement("target")
  }
}
