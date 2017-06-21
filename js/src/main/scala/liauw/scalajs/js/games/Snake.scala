package liauw.scalajs.js.games

import org.scalajs.dom
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Canvas

import scalatags.JsDom.all._

import Direction.{ Direction, Down, Left, Right, Up, parseDirection }

object Direction extends Enumeration {
  type Direction = Value
  val Left, Right, Up, Down = Value

  def parseDirection: PartialFunction[Int, Direction] = {
    case 37 => Left //left
    case 38 => Up //up
    case 39 => Right //right
    case 40 => Down //down
  }
}

case class Board(width: Int, height: Int)
case class Position(x: Int, y: Int) {
  def goDirection(dir: Direction, board: Board): Position = {
    dir match {
      case Left if x == 0 => Position(board.width-1, y)
      case Left => Position(x-1, y)
      case Right if x >= board.width-1 => Position(0, y)
      case Right => Position(x+1, y)
      case Up if y == 0 => Position(x, board.height-1)
      case Up => Position(x, y-1)
      case Down if y >= board.height-1 => Position(x, 0)
      case Down => Position(x, y+1)
    }
  }
}

object Snake {
  def addToElement(id: String): Unit = {
    var board: Board = Board(20,20)
    var direction: Direction = Right
    var position: Position = Position(0,0)
    var tail: Vector[Position] = Vector(position)
    def newApplePosition(tail: Vector[Position], board: Board): Position = {
      def randomPosition() = Position((Math.random() * board.width).toInt, (math.random() * board.height).toInt)
      var newPosition = randomPosition()
      while(tail.contains(newPosition)) {
        newPosition = randomPosition()
      }
      newPosition
    }
    var apple: Position = newApplePosition(tail, board)

    val snakeDiv = dom.document.getElementById(id)

    val keyDown = { event: dom.KeyboardEvent =>
      if(parseDirection.isDefinedAt(event.keyCode)) direction = parseDirection(event.keyCode)
    }
    val mouseOver = { event: dom.MouseEvent =>
      println((event.screenX,event.screenY))
    }
    dom.document.addEventListener("keydown", keyDown)
    val canvasWidth = 400
    val canvasHeight = 400

    val canv = canvas(onmousemove := mouseOver)(attr("width") := canvasWidth, attr("height") := canvasHeight).render
    val ctx = canv.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    snakeDiv.appendChild(canv)

    def render(canvas: Canvas) = {
      val tileWidth = canvasWidth / board.width
      val tileHeight = canvasHeight / board.height

      ctx.fillStyle = "black"
      ctx.fillRect(0, 0, canvas.width, canvas.height)

      ctx.fillStyle = "lime"
      tail.foreach { pos =>
        val newX = pos.x * tileWidth
        val newY = pos.y * tileHeight
        ctx.fillRect(newX+1, newY+1, tileWidth-2, tileHeight-2)
      }
      ctx.fillStyle = "red"
      ctx.fillRect(apple.x * tileWidth, apple.y * tileHeight, tileWidth, tileHeight)
    }

    val runRound = { () =>
      position = position.goDirection(direction, board)
      position match {
        case pos if tail.drop(1).contains(pos) =>
          tail = tail.takeRight(5)
        case pos if pos == apple =>
          tail :+= position
          apple = newApplePosition(tail, board)
        case pos =>
          tail :+= position
          tail = tail.drop(1)
      }
      render(canv)
    }
    render(canv)

    dom.window.setInterval(runRound, 1000/15)
  }
}