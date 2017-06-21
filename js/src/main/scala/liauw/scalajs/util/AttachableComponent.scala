package liauw.scalajs.util

import org.scalajs.dom
import org.scalajs.dom.Node

trait AttachableComponent {
  def child: Node

  def addToElement(id: String): Unit  = {
    dom.document.getElementById(id).appendChild(child)
  }
}
