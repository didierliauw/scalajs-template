package liauw.scalajs.jvm.util

import ActorGraphStage.{ GraphStageStarted, WSMessage }
import akka.actor.ActorRef
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.stream._
import akka.stream.stage._

object ActorGraphStage {
  case class GraphStageStarted(graphStageActor: ActorRef)
  case class WSMessage(text: String)
}

class ActorGraphStage(val handlerActor: ActorRef) extends GraphStage[FlowShape[Message,Message]] {
  val in = Inlet[Message]("stage.in")
  val out = Outlet[Message]("stage.out")

  override val shape = FlowShape.of(in, out)
  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    var buffer: Vector[Any] = Vector()

    new GraphStageLogic(shape) {
      override def preStart() = {
        val actor = getStageActor { case (ref: ActorRef, message: Any) =>
          buffer :+= message
          if(isAvailable(out)) {
            push(out, TextMessage.Strict(buffer.head.toString))
            buffer = buffer.tail
          }
        }
        handlerActor ! GraphStageStarted(actor.ref)
        pull(in)
      }

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val message = grab(in)
          handlerActor ! WSMessage(message.asTextMessage.getStrictText)
          if(!hasBeenPulled(in)) pull(in)
        }
      })
      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          if(buffer.nonEmpty && isAvailable(out)) {
            push(out, TextMessage.Strict(buffer.head.toString))
            buffer = buffer.tail
          }
        }
      })
    }
  }
}
