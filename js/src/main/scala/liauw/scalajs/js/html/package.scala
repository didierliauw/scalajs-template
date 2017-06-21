package liauw.scalajs.js

import org.scalajs.dom
import org.scalajs.dom.raw.FileReader
import org.scalajs.dom.{ Event, UIEvent, Node, File }

import scala.concurrent.{ Promise, Future }
import scala.scalajs.js
import scala.scalajs.js.UndefOr

package object html {
  implicit class NodeOps(val node: Node) extends AnyVal {
    def makeFileDroppable(fileHandler: File => Unit): Unit = {
      def handleDragOver = (event: dom.DragEvent) => {
        event.stopPropagation()
        event.preventDefault()
        event.dataTransfer.dropEffect = "copy"; // Explicitly show this is a copy.
      }

      def handleDrop = (event: dom.DragEvent) => {
        event.stopPropagation()
        event.preventDefault()

        val files = event.dataTransfer.files; // FileList object.

        // files is a FileList of File objects. List some properties.
        (0 until files.length).foreach { index =>
          val file = files(index)
          fileHandler(file)
        }
      }

      // Setup the dnd listeners.
      node.addEventListener("dragover", handleDragOver, false)
      node.addEventListener("drop", handleDrop, false)
    }
  }

  implicit class FileOps(val file: File) extends AnyVal {
    def debugString(): String = {
      val t = UndefOr.any2undefOrA(file.`type`).getOrElse("n/a")
      val date = file.lastModifiedDate.asInstanceOf[js.Date].toLocaleDateString()
      s"""${file.name}, type: $t, size: ${file.size} bytes, last modified: $date"""
    }

    def read(): Future[String] = {
      val promise = Promise[String]

      val reader = new FileReader()
      reader.onload = (event: UIEvent) => {
        val result = reader.result.asInstanceOf[String]
        promise.success(result)
      }
      reader.onerror = (event: Event) => {
        promise.failure(new Exception(reader.error.name))
      }
      reader.readAsText(file)
      promise.future
    }
  }
}
